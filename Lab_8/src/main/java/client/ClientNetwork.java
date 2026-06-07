package client;

import common.Response;
import common.commands.Command;

import java.io.*;
import java.net.Socket;

public class ClientNetwork implements AutoCloseable {
    private final String host;
    private final int port;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    public ClientNetwork(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        connect();
    }

    private void connect() throws IOException {
        int attempts = 5;
        while (true) {
            try {
                socket = new Socket(host, port);
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                return;
            } catch (IOException e) {
                if (--attempts == 0) throw e;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public synchronized Response sendCommand(Command command) throws IOException, ClassNotFoundException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(command);
            oos.close();
            byte[] data = baos.toByteArray();
            dos.writeInt(data.length);
            dos.write(data);
            dos.flush();

            int length = dis.readInt();
            byte[] respData = new byte[length];
            dis.readFully(respData);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(respData));
            return (Response) ois.readObject();
        } catch (IOException e) {
            try {
                connect();
            } catch (IOException ignored) {
                throw new IOException("Не удалось восстановить соединение с сервером. Команда не выполнена.", e);
            }
            throw new IOException("Соединение с сервером было потеряно. Пожалуйста, повторите команду.", e);
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
