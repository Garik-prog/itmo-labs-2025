package commands;

import manager.CollectionManager;
import console.InputProvider;
import models.Flat;

/**
 * Команда для обновления элемента коллекции по его id.
 * Сохраняет старые id и дату создания.
 *
 * @see AbstractCommand
 */
public class UpdateCommand extends AbstractCommand {

    /**
     * Создаёт команду update.
     *
     * @param collectionManager менеджер коллекции
     */
    public UpdateCommand(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /**
     * Выполняет команду update.
     * Находит ключ по id, читает новый объект Flat,
     * сохраняет id и дату создания старого объекта, обновляет элемент.
     *
     * @param args аргументы команды (ожидается id)
     * @param provider источник ввода для чтения объекта
     * @throws Exception если произошла ошибка ввода
     */
    @Override
    public void execute(String[] args, InputProvider provider) throws Exception {
        if (!validateArgs(args, 1, "укажите id. Использование: update <id>")) return;
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: id должно быть целым числом.");
            return;
        }

        String key = collectionManager.getKeyById(id);
        if (key == null) {
            System.out.println("Элемент с таким id не найден.");
            return;
        }

        Flat oldFlat = collectionManager.get(key);
        Flat newFlat = readFlat(provider);
        newFlat.setId(oldFlat.getId());
        newFlat.setCreationDate(oldFlat.getCreationDate());
        collectionManager.updateByKey(key, newFlat);
        System.out.println("Элемент обновлён.");
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "обновить значение элемента по его id";
    }
}