package commands;

import manager.CollectionManager;
import console.InputProvider;
import java.nio.*;

/**
 * Команда для очистки коллекции (удаления всех элементов).
 *
 * @see AbstractCommand
 */
public class ClearCommand extends AbstractCommand {

    /**
     * Создаёт команду clear.
     *
     * @param collectionManager менеджер коллекции
     */
    public ClearCommand(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /**
     * Выполняет команду clear – удаляет все элементы из коллекции.
     *
     * @param args аргументы команды (не используются)
     * @param provider источник ввода (не используется)
     */
    @Override
    public void execute(String[] args, InputProvider provider) {
        collectionManager.clear();
        System.out.println("Коллекция очищена.");
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }
}