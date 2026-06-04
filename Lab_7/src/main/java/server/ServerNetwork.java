package server;

import common.Response;
import common.commands.Command;
import common.commands.RegisterCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ServerNetwork {
    private static final Logger logger = LoggerFactory.getLogger(ServerNetwork.class);

    private final int port;
    private final CollectionManager collectionManager;
    private final ForkJoinPool readPool = new ForkJoinPool();
    private final ExecutorService executorPool = Executors.newCachedThreadPool();
    private final ExecutorService senderPool = Executors.newCachedThreadPool();
    private volatile boolean running = true;
    private ServerSocket serverSocket;

    public ServerNetwork(int port, CollectionManager cm) {
        this.port = port;
        this.collectionManager = cm;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        logger.info("Сервер запущен на порту {}", port);
        while (running) {
            try {
                Socket client = serverSocket.accept();
                logger.info("Новое подключение: {}", client.getRemoteSocketAddress());
                readPool.submit(new ClientHandler(client));
            } catch (IOException e) {
                if (running) logger.error("Ошибка принятия соединения", e);
            }
        }
    }

    public void stop() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException ignored) {
        }
        readPool.shutdown();
        executorPool.shutdown();
        senderPool.shutdown();
    }

    private Response processCommand(Command cmd) {
        collectionManager.addHistory(cmd.getName());

        if (cmd instanceof RegisterCommand) {
            return cmd.execute(collectionManager);
        }

        String login = cmd.getLogin();
        String password = cmd.getPassword();

        if (login == null || password == null
                || !collectionManager.getUserManager().authenticate(login, password)) {
            return new Response("Ошибка авторизации. Укажите корректный логин и пароль.");
        }

        return cmd.execute(collectionManager);
    }

    private void sendResponse(DataOutputStream dos, Response resp) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(resp);
            oos.close();
            byte[] data = baos.toByteArray();
            synchronized (dos) {
                dos.writeInt(data.length);
                dos.write(data);
                dos.flush();
            }
        } catch (IOException e) {
            logger.error("Ошибка отправки ответа: {}", e.getMessage());
        }
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                while (!socket.isClosed()) {
                    int length;
                    try {
                        length = dis.readInt();
                    } catch (EOFException | SocketException e) {
                        break;
                    }
                    byte[] data = new byte[length];
                    dis.readFully(data);

                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                    Command cmd = (Command) ois.readObject();
                    logger.debug("Получена команда: {} от {}", cmd.getName(), cmd.getLogin());

                    executorPool.submit(() -> {
                        Response resp = processCommand(cmd);
                        senderPool.submit(() -> sendResponse(dos, resp));
                    });
                }
            } catch (Exception e) {
                logger.debug("Клиент отключился: {}", e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
