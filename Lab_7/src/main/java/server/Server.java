package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws Exception {
        String dbHost     = args.length > 0 ? args[0] : "pg";
        String dbName     = args.length > 1 ? args[1] : "studs";
        String dbUser     = args.length > 2 ? args[2] : System.getProperty("user.name");
        String dbPassword = args.length > 3 ? args[3] : "";

        logger.info("Подключение к БД: jdbc:postgresql://{}/{} как {}", dbHost, dbName, dbUser);
        DatabaseManager db = new DatabaseManager(dbHost, dbName, dbUser, dbPassword);
        UserManager um = new UserManager(db);
        CollectionManager cm = new CollectionManager();
        cm.setDatabaseManager(db);
        cm.setUserManager(um);

        try {
            cm.setCollection(db.loadAllFlats());
            logger.info("Загружено {} объектов из БД.", cm.getSize());
        } catch (Exception e) {
            logger.error("Ошибка загрузки коллекции из БД, продолжаем с пустой: {}", e.getMessage());
        }

        ServerNetwork serverNetwork = new ServerNetwork(5433, cm);

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine().trim();
                if ("exit".equals(line)) {
                    logger.info("Завершение сервера.");
                    serverNetwork.stop();
                    break;
                }
            }
        }, "console-thread").start();

        serverNetwork.start();
    }
}
