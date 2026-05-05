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
        String host = "localhost";
        int port = 12345;
        if (args.length > 0) host = args[0];
        if (args.length > 1) port = Integer.parseInt(args[1]);

        try (ClientNetwork network = new ClientNetwork(host, port);
             InputProvider console = new ConsoleInputProvider()) {

            CommandParser parser = new CommandParser(console);

            while (true) {
                System.out.print("> ");
                String line = console.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("execute_script ")) {
                    String scriptFile = line.substring("execute_script ".length()).trim();
                    executeScript(scriptFile, network);
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

    private static void executeScript(String filename, ClientNetwork network) {
        if (recursionDepth >= MAX_SCRIPT_DEPTH) {
            System.out.println("Превышена максимальная глубина рекурсии скриптов.");
            return;
        }
        recursionDepth++;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            InputProvider fileProvider = new FileInputProvider(filename);
            CommandParser parser = new CommandParser(fileProvider);

            for (String rawLine : lines) {
                String line = rawLine.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                System.out.println("> " + line);
                try {
                    Command cmd = parser.parseCommand(line);
                    if (cmd == null) continue;
                    if (cmd instanceof ExitCommand) {
                        System.out.println("Команда exit в скрипте пропущена.");
                        continue;
                    }
                    if (cmd instanceof HelpCommand) {
                        printHelp();
                        continue;
                    }
                    Response resp = network.sendCommand(cmd);
                    outputResponse(resp);
                } catch (Exception e) {
                    System.out.println("Ошибка в скрипте: " + e.getMessage());
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
        System.out.println(resp.getMessage());
        if (resp.getFlats() != null) {
            for (common.models.Flat f : resp.getFlats()) {
                System.out.println(f);
            }
        }
    }

    private static void printHelp() {
        System.out.println("Доступные команды:");
        System.out.println("  help");
        System.out.println("  info");
        System.out.println("  show");
        System.out.println("  insert <key>");
        System.out.println("  update <id>");
        System.out.println("  remove_key <key>");
        System.out.println("  clear");
        System.out.println("  execute_script <file>");
        System.out.println("  exit");
        System.out.println("  remove_lower");
        System.out.println("  history");
        System.out.println("  replace_if_lower <key>");
        System.out.println("  min_by_creation_date");
        System.out.println("  count_by_view <view>");
        System.out.println("  print_ascending");
    }
}