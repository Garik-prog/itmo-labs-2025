package commands;

import console.InputProvider;

/**
 * Команда для завершения программы без сохранения.
 *
 * @see Command
 */
public class ExitCommand implements Command {

    /**
     * Выполняет команду exit – завершает программу с кодом 0.
     *
     * @param args аргументы команды (не используются)
     * @param provider источник ввода (не используется)
     */
    @Override
    public void execute(String[] args, InputProvider provider) {
        System.out.println("Завершение программы.");
        System.exit(0);
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "завершить программу (без сохранения)";
    }
}