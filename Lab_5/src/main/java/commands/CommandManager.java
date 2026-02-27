package commands;

import console.InputProvider;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Менеджер команд. Хранит зарегистрированные команды и историю их выполнения.
 * Отвечает за диспетчеризацию введённых пользователем команд к соответствующим обработчикам.
 *
 * @see Command
 */
public class CommandManager {

    /** Карта зарегистрированных команд (имя команды -> объект команды) */
    private final Map<String, Command> commands = new HashMap<>();

    /** История последних 11 выполненных команд (без аргументов) */
    private final Deque<String> history = new ArrayDeque<>(11);

    /**
     * Регистрирует команду в менеджере.
     *
     * @param name имя команды
     * @param command объект команды
     */
    public void registerCommand(String name, Command command) {
        commands.put(name, command);
    }

    /**
     * Выполняет команду по строке ввода.
     * Добавляет имя команды в историю, находит команду по имени,
     * выделяет аргументы и вызывает метод execute найденной команды.
     *
     * @param line полная строка ввода (имя команды и аргументы)
     * @param provider источник ввода для передачи команде
     * @throws Exception если произошла ошибка при выполнении команды
     */
    public void executeCommand(String line, InputProvider provider) throws Exception {
        String[] parts = line.trim().split("\\s+");
        if (parts.length == 0) return;
        String cmdName = parts[0];

        // Добавляем команду в историю (циклическая очередь из 11 элементов)
        if (history.size() == 11) {
            history.pollFirst();
        }
        history.addLast(cmdName);

        Command cmd = commands.get(cmdName);
        if (cmd == null) {
            System.out.println("Неизвестная команда. Введите help для справки.");
            return;
        }

        // Выделяем аргументы (всё после имени команды)
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, parts.length - 1);
        cmd.execute(args, provider);
    }

    /**
     * Выводит в консоль историю последних 11 выполненных команд.
     */
    public void printHistory() {
        System.out.println("Последние 11 команд:");
        for (String cmd : history) {
            System.out.println(cmd);
        }
    }

    /**
     * Возвращает карту всех зарегистрированных команд.
     *
     * @return карта команд
     */
    public Map<String, Command> getCommands() {
        return commands;
    }
}