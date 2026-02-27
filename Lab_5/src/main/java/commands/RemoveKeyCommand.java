package commands;

import manager.CollectionManager;
import console.InputProvider;

/**
 * Команда для удаления элемента из коллекции по ключу.
 *
 * @see AbstractCommand
 */
public class RemoveKeyCommand extends AbstractCommand {

    /**
     * Создаёт команду remove_key.
     *
     * @param collectionManager менеджер коллекции
     */
    public RemoveKeyCommand(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /**
     * Выполняет команду remove_key.
     * Проверяет наличие ключа и удаляет соответствующий элемент.
     *
     * @param args аргументы команды (ожидается ключ)
     * @param provider источник ввода (не используется)
     */
    @Override
    public void execute(String[] args, InputProvider provider) {
        if (!validateArgs(args, 1, "укажите ключ. Использование: remove_key <key>")) return;
        String key = args[0];
        if (!collectionManager.containsKey(key)) {
            System.out.println("Ключ не найден.");
            return;
        }
        collectionManager.removeByKey(key);
        System.out.println("Элемент удалён.");
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "удалить элемент по ключу";
    }
}