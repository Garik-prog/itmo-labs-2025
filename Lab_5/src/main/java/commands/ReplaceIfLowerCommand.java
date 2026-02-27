package commands;

import manager.CollectionManager;
import console.InputProvider;
import models.Flat;

/**
 * Команда для замены значения по ключу, если новое значение меньше старого.
 * Сохраняет id и дату создания старого объекта.
 *
 * @see AbstractCommand
 */
public class ReplaceIfLowerCommand extends AbstractCommand {

    /**
     * Создаёт команду replace_if_lowe.
     *
     * @param collectionManager менеджер коллекции
     */
    public ReplaceIfLowerCommand(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /**
     * Выполняет команду replace_if_lowe.
     * Проверяет наличие ключа, читает новый объект Flat,
     * сравнивает с существующим и заменяет, если новый меньше.
     *
     * @param args аргументы команды (ожидается ключ)
     * @param provider источник ввода для чтения объекта
     * @throws Exception если произошла ошибка ввода
     */
    @Override
    public void execute(String[] args, InputProvider provider) throws Exception {
        if (!validateArgs(args, 1, "укажите ключ. Использование: replace_if_lowe <key>")) return;
        String key = args[0];
        Flat oldFlat = collectionManager.get(key);
        if (oldFlat == null) {
            System.out.println("Ключ не найден.");
            return;
        }

        Flat newFlat = readFlat(provider);
        newFlat.setId(oldFlat.getId());
        newFlat.setCreationDate(oldFlat.getCreationDate());

        if (collectionManager.replaceIfLower(key, newFlat)) {
            System.out.println("Значение заменено.");
        } else {
            System.out.println("Новое значение не меньше старого. Замена не выполнена.");
        }
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "заменить значение по ключу, если новое значение меньше старого";
    }
}