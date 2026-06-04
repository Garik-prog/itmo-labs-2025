package client;

import common.Response;
import common.commands.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Client {
    private static final int MAX_SCRIPT_DEPTH = 10;
    private static int recursionDepth = 0;

    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5433;

        try (ClientNetwork network = new ClientNetwork(host, port);
             ConsoleInputProvider console = new ConsoleInputProvider()) {

            String[] credentials = authenticate(network, console);
            if (credentials == null) return;

            String login = credentials[0];
            String password = credentials[1];

            CommandParser parser = new CommandParser(console, login, password);

            while (true) {
                System.out.print("[" + login + "]> ");
                String line = console.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("execute_script ")) {
                    String scriptFile = line.substring("execute_script ".length()).trim();
                    executeScript(scriptFile, network, login, password);
                    continue;
                }

                if ("help".equals(line)) {
                    printHelp();
                    continue;
                }

                try {
                    Command cmd = parser.parseCommand(line);
                    if (cmd == null) continue;
                    if (cmd instanceof ExitCommand) {
                        System.out.println("Завершение программы.");
                        return;
                    }
                    if (cmd instanceof HelpCommand) {
                        printHelp();
                        continue;
                    }
                    Response resp = network.sendCommand(cmd);
                    outputResponse(resp);
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
        }
    }


    private static String[] authenticate(ClientNetwork network, ConsoleInputProvider console) throws IOException {
        while (true) {
            System.out.println("=== Добро пожаловать ===");
            System.out.println("1. Войти");
            System.out.println("2. Зарегистрироваться");
            System.out.println("3. Выйти");
            System.out.print("Выбор: ");
            String choiceLine = console.readLine();
            if (choiceLine == null) return null;
            String choice = choiceLine.trim();

            if ("3".equals(choice)) return null;

            if (!"1".equals(choice) && !"2".equals(choice)) {
                System.out.println("Ошибка: введите 1, 2 или 3.");
            }

            System.out.print("Логин: ");
            String login = console.readLine();
            if (login == null) return null;
            login = login.trim();

            System.out.print("Пароль: ");
            String password = console.readLine();
            if (password == null) return null;

            if ("2".equals(choice)) {
                RegisterCommand reg = new RegisterCommand(login, password);
                try {
                    Response r = network.sendCommand(reg);
                    System.out.println(r.message());
                    if (!r.message().contains("успешно")) continue;
                } catch (Exception e) {
                    System.out.println("Ошибка связи: " + e.getMessage());
                    continue;
                }
            }

            InfoCommand info = new InfoCommand();
            info.setCredentials(login, password);
            try {
                Response r = network.sendCommand(info);
                if (r.message().contains("авторизации")) {
                    System.out.println("Неверный логин или пароль. Попробуйте снова.");
                    continue;
                }
                System.out.println("Вход выполнен как: " + login);
                return new String[]{login, password};
            } catch (Exception e) {
                System.out.println("Ошибка связи: " + e.getMessage());
            }
        }
    }

    private static void executeScript(String filename, ClientNetwork network, String login, String password) {
        if (recursionDepth >= MAX_SCRIPT_DEPTH) {
            System.out.println("Превышена максимальная глубина рекурсии скриптов.");
            return;
        }
        recursionDepth++;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            try (FileInputProvider fileProvider = new FileInputProvider(filename)) {
                CommandParser parser = new CommandParser(fileProvider, login, password);
                for (String rawLine : lines) {
                    String line = rawLine.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    System.out.println("> " + line);
                    if (line.startsWith("execute_script ")) {
                        executeScript(line.substring("execute_script ".length()).trim(), network, login, password);
                        continue;
                    }
                    try {
                        Command cmd = parser.parseCommand(line);
                        if (cmd == null) continue;
                        if (cmd instanceof ExitCommand || cmd instanceof HelpCommand) continue;
                        Response resp = network.sendCommand(cmd);
                        outputResponse(resp);
                    } catch (Exception e) {
                        System.out.println("Ошибка в скрипте: " + e.getMessage());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл скрипта не найден: " + filename);
        } catch (IOException e) {
            System.out.println("Ошибка чтения скрипта: " + e.getMessage());
        } finally {
            recursionDepth--;
        }
    }

    private static void outputResponse(Response resp) {
        System.out.println(resp.message());
        if (resp.flats() != null) {
            for (common.models.Flat f : resp.flats()) {
                System.out.println("  " + f);
            }
        }
    }

    private static void printHelp() {
        System.out.println("Доступные команды:");
        System.out.println("  help                       — справка");
        System.out.println("  info                       — информация о коллекции");
        System.out.println("  show                       — показать все элементы");
        System.out.println("  insert <key>               — добавить элемент");
        System.out.println("  update <id>                — обновить элемент по id");
        System.out.println("  remove_key <key>           — удалить по ключу");
        System.out.println("  clear                      — удалить свои элементы");
        System.out.println("  execute_script <file>      — выполнить скрипт");
        System.out.println("  exit                       — выйти");
        System.out.println("  remove_lower               — удалить свои элементы меньше заданного");
        System.out.println("  history                    — история команд");
        System.out.println("  replace_if_lower <key>     — заменить если новый меньше");
        System.out.println("  min_by_creation_date       — элемент с мин. датой создания");
        System.out.println("  count_by_view <view>       — количество по view");
        System.out.println("  print_ascending            — вывести в порядке возрастания");
    }
}
