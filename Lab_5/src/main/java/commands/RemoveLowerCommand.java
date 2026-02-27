package commands;

import manager.CollectionManager;
import console.InputProvider;
import models.Flat;

/**
 * Команда для удаления всех элементов, меньших заданного.
 * Сравнение выполняется по естественному порядку Flat.
 *
 * @see AbstractCommand
 */
public class RemoveLowerCommand extends AbstractCommand {

    /**
     * Создаёт команду remove_lower.
     *
     * @param collectionManager менеджер коллекции
     */
    public RemoveLowerCommand(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /**
     * Выполняет команду remove_lower.
     * Читает эталонный объект Flat и удаляет все элементы, меньшие его.
     *
     * @param args аргументы команды (не используются)
     * @param provider источник ввода для чтения объекта
     * @throws Exception если произошла ошибка ввода
     */
    @Override
    public void execute(String[] args, InputProvider provider) throws Exception {
        Flat flat = readFlat(provider);
        collectionManager.removeLower(flat);
        System.out.println("Элементы, меньшие заданного, удалены.");
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, меньшие, чем заданный";
    }
}