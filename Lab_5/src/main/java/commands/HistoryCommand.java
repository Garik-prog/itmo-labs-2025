package commands;

import console.InputProvider;

/**
 * Команда для вывода истории последних 11 команд.
 *
 * @see Command
 * @see CommandManager
 */
public class HistoryCommand implements Command {

    /** Менеджер команд для получения истории */
    private final CommandManager manager;

    /**
     * Создаёт команду history.
     *
     * @param manager менеджер команд
     */
    public HistoryCommand(CommandManager manager) {
        this.manager = manager;
    }

    /**
     * Выполняет команду history – выводит последние 11 команд.
     *
     * @param args аргументы команды (не используются)
     * @param provider источник ввода (не используется)
     */
    @Override
    public void execute(String[] args, InputProvider provider) {
        manager.printHistory();
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "вывести последние 11 команд";
    }
}