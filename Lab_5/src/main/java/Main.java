import manager.CollectionManager;
import commands.*;
import console.ConsoleInputProvider;
import console.InputProvider;
import manager.FileManager;
import models.Flat;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Главный класс приложения для управления коллекцией объектов Flat.
 * Программа загружает данные из XML-файла, указанного в аргументе командной строки,
 * и предоставляет интерактивный интерфейс для управления коллекцией.
 *
 * @version 1.0
 * @author Ваше имя
 */
public class Main {

    /**
     * Точка входа в программу.
     *
     * @param args аргументы командной строки, первый аргумент - имя файла с данными
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Укажите имя файла как аргумент командной строки.");
            return;
        }

        String filename = args[0];

        CollectionManager collectionManager = new CollectionManager();
        FileManager fileManager = new FileManager(filename);

        // Загрузка коллекции
        try {
            LinkedHashMap<String, Flat> loaded = fileManager.load();
            collectionManager.setCollection(loaded);
            System.out.println("Коллекция загружена. Элементов: " + loaded.size());
        } catch (Exception e) {
            System.out.println("Ошибка загрузки из файла: " + e.getMessage());
            System.out.println("Продолжаем с пустой коллекцией.");
        }

        // Создание менеджера команд и регистрация
        CommandManager cmdManager = new CommandManager();
        cmdManager.registerCommand("help", new HelpCommand(cmdManager));
        cmdManager.registerCommand("info", new InfoCommand(collectionManager));
        cmdManager.registerCommand("show", new ShowCommand(collectionManager));
        cmdManager.registerCommand("insert", new InsertCommand(collectionManager));
        cmdManager.registerCommand("update", new UpdateCommand(collectionManager));
        cmdManager.registerCommand("remove_key", new RemoveKeyCommand(collectionManager));
        cmdManager.registerCommand("clear", new ClearCommand(collectionManager));
        cmdManager.registerCommand("save", new SaveCommand(collectionManager, fileManager));
        cmdManager.registerCommand("execute_script", new ExecuteScriptCommand(cmdManager));
        cmdManager.registerCommand("exit", new ExitCommand());
        cmdManager.registerCommand("remove_lower", new RemoveLowerCommand(collectionManager));
        cmdManager.registerCommand("history", new HistoryCommand(cmdManager));
        cmdManager.registerCommand("replace_if_lower", new ReplaceIfLowerCommand(collectionManager));
        cmdManager.registerCommand("min_by_creation_date", new MinByCreationDateCommand(collectionManager));
        cmdManager.registerCommand("count_by_view", new CountByViewCommand(collectionManager));
        cmdManager.registerCommand("print_ascending", new PrintAscendingCommand(collectionManager));

        // Интерактивный цикл ввода команд
        try (InputProvider provider = new ConsoleInputProvider()) {
            while (true) {
                System.out.print("> ");
                String line = provider.readLine();
                if (line == null) break; // Ctrl+D
                if (line.trim().isEmpty()) continue;
                try {
                    cmdManager.executeCommand(line, provider);
                } catch (Exception e) {
                    System.out.println("Ошибка выполнения команды: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
        }
    }
}