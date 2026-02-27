package commands;

import console.InputProvider;

/**
 * Команда для вывода справки по всем доступным командам.
 *
 * @see Command
 */
public class HelpCommand implements Command {

    /** Менеджер команд для получения списка всех команд */
    private final CommandManager manager;

    /**
     * Создаёт команду help.
     *
     * @param manager менеджер команд
     */
    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    /**
     * Выводит список всех зарегистрированных команд с их описаниями.
     *
     * @param args аргументы команды (не используются)
     * @param provider источник ввода (не используется)
     */
    @Override
    public void execute(String[] args, InputProvider provider) {
        System.out.println("Доступные команды:");
        manager.getCommands().forEach((name, cmd) ->
                System.out.println("  " + name + " : " + cmd.getDescription()));
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "вывести справку по доступным командам";
    }
}