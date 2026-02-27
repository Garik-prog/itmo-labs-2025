package commands;

import console.FileInputProvider;
import console.InputProvider;

import java.io.FileNotFoundException;

/**
 * Команда для выполнения скрипта из файла.
 * Поддерживает рекурсивный вызов скриптов с ограничением глубины.
 *
 * @see Command
 * @see CommandManager
 */
public class ExecuteScriptCommand implements Command {

    /** Менеджер команд для выполнения команд из скрипта */
    private final CommandManager commandManager;

    /** Максимальная глубина рекурсии скриптов */
    private static final int MAX_DEPTH = 10;

    /** Текущая глубина рекурсии (статистическая для всех вызовов) */
    private static int recursionDepth = 0;

    /**
     * Создаёт команду execute_script.
     *
     * @param commandManager менеджер команд
     */
    public ExecuteScriptCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду execute_script.
     * Открывает файл, читает команды построчно и выполняет их через менеджер команд.
     * Предотвращает бесконечную рекурсию с помощью счётчика глубины.
     *
     * @param args аргументы команды (ожидается имя файла)
     * @param provider источник ввода (не используется, заменяется файловым)
     * @throws Exception если произошла ошибка при выполнении
     */
    @Override
    public void execute(String[] args, InputProvider provider) throws Exception {
        if (args.length < 1) {
            System.out.println("Ошибка: укажите имя файла. Использование: execute_script <file_name>");
            return;
        }

        String filename = args[0];

        if (recursionDepth >= MAX_DEPTH) {
            System.out.println("Ошибка: превышена максимальная глубина рекурсии скриптов (" + MAX_DEPTH + ")");
            return;
        }

        recursionDepth++;
        try (InputProvider scriptProvider = new FileInputProvider(filename)) {
            String line;
            while ((line = scriptProvider.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                System.out.println("> " + line);
                commandManager.executeCommand(line, scriptProvider);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка: файл не найден.");
        } finally {
            recursionDepth--;
        }
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "считать и исполнить скрипт из указанного файла";
    }
}