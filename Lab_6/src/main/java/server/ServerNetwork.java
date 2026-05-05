package server;

import common.Response;
import common.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.Iterator;

public class ServerNetwork {
    private static final Logger logger = LoggerFactory.getLogger(ServerNetwork.class);
    private final int port;
    private final CollectionManager collectionManager;
    private final Selector selector;
    private volatile boolean running = true;

    public ServerNetwork(int port, CollectionManager cm) throws IOException {
        this.port = port;
        this.collectionManager = cm;
        this.selector = Selector.open();

        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        logger.info("Серверный канал запущен на порту {}", port);
    }

    public void start() throws IOException {
        logger.info("Сервер начал работу.");
        while (running) {
            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (!key.isValid()) continue;
                try {
                    if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable()) {
                        read(key);
                    } else if (key.isWritable()) {
                        write(key);
                    }
                } catch (IOException e) {
                    logger.error("Ошибка ввода-вывода", e);
                    key.cancel();
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(false);
        ClientContext ctx = new ClientContext();
        sc.register(selector, SelectionKey.OP_READ, ctx);
        logger.info("Новое подключение: {}", sc.getRemoteAddress());
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        ClientContext ctx = (ClientContext) key.attachment();
        ByteBuffer buf = ctx.buffer;
        int n = sc.read(buf);
        if (n == -1) {
            sc.close();
            return;
        }
        if (ctx.state == ClientContext.State.READ_LENGTH && buf.position() >= 4) {
            buf.flip();
            ctx.dataLength = buf.getInt();
            buf.compact();
            ctx.state = ClientContext.State.READ_DATA;
            if (buf.capacity() < ctx.dataLength) {
                buf = ByteBuffer.allocate(ctx.dataLength);
                ctx.buffer = buf;
            }
        }
        if (ctx.state == ClientContext.State.READ_DATA && buf.position() >= ctx.dataLength) {
            buf.flip();
            byte[] data = new byte[ctx.dataLength];
            buf.get(data);
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            try {
                Command cmd = (Command) ois.readObject();
                logger.debug("Получена команда {}", cmd.getName());
                collectionManager.addHistory(cmd.getName());
                Response resp = cmd.execute(collectionManager);
                sendResponse(key, resp);
            } catch (Exception e) {
                logger.error("Ошибка выполнения команды", e);
                sendResponse(key, new Response("Ошибка сервера"));
            }
            ctx.state = ClientContext.State.READ_LENGTH;
            ctx.buffer.clear();
        }
    }

    private void sendResponse(SelectionKey key, Response resp) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(resp);
        oos.close();
        byte[] data = baos.toByteArray();
        ByteBuffer head = ByteBuffer.allocate(4 + data.length);
        head.putInt(data.length);
        head.put(data);
        head.flip();
        ClientContext ctx = (ClientContext) key.attachment();
        ctx.pendingResponse = head;
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        ClientContext ctx = (ClientContext) key.attachment();
        ByteBuffer buf = ctx.pendingResponse;
        if (buf == null) {
            key.interestOps(SelectionKey.OP_READ);
            return;
        }
        sc.write(buf);
        if (!buf.hasRemaining()) {
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    public void stop() {
        running = false;
        selector.wakeup();
    }
}