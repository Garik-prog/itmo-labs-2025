package commands;

import console.InputProvider;

/**
 * Интерфейс для всех команд приложения.
 * Определяет контракт, который должны реализовывать все команды.
 *
 * @see AbstractCommand
 * @see CommandManager
 */
public interface Command {

    /**
     * Выполняет команду с заданными аргументами и источником ввода.
     *
     * @param args массив аргументов команды (без имени команды)
     * @param provider источник ввода для чтения дополнительных данных
     * @throws Exception если в процессе выполнения произошла ошибка
     */
    void execute(String[] args, InputProvider provider) throws Exception;

    /**
     * Возвращает описание команды для вывода в справке.
     *
     * @return строковое описание команды
     */
    String getDescription();
}