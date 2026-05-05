package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Scanner;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Укажите имя файла как аргумент командной строки.");
            return;
        }
        String filename = args[0];
        CollectionManager cm = new CollectionManager();
        FileManager fm = new FileManager(filename);
        try {
            cm.setCollection(fm.load());
        } catch (Exception e) {
            logger.error("Ошибка загрузки коллекции: {}", e.getMessage());
            System.out.println("Продолжаем с пустой коллекцией.");
        }

        ServerNetwork serverNetwork = new ServerNetwork(12345, cm);
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if ("save".equals(line)) {
                    try {
                        fm.save(cm.getCollection());
                        logger.info("Коллекция сохранена.");
                        System.out.println("Коллекция сохранена.");
                    } catch (IOException ex) {
                        logger.error("Ошибка сохранения", ex);
                    }
                } else if ("exit".equals(line)) {
                    try {
                        fm.save(cm.getCollection());
                        logger.info("Коллекция сохранена при выходе.");
                    } catch (IOException ignored) {}
                    serverNetwork.stop();
                    break;
                }
            }
        }).start();

        serverNetwork.start();
    }
}