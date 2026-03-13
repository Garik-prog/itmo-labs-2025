package commands;

import manager.CollectionManager;
import console.InputProvider;
import models.Flat;

import java.util.Date;

/**
 * Команда для добавления нового элемента в коллекцию с заданным ключом.
 * Генерирует id и дату создания автоматически.
 */
public class InsertCommand extends AbstractCommand {

    /**
     * Создаёт команду insert.
     *
     * @param collectionManager менеджер коллекции
     */
    public InsertCommand(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /**
     * Выполняет команду insert.
     * Проверяет уникальность ключа, читает объект Flat, устанавливает id и дату,
     * добавляет в коллекцию.
     *
     * @param args аргументы команды (ожидается ключ)
     * @param provider источник ввода
     * @throws Exception если произошла ошибка ввода
     */
    @Override
    public void execute(String[] args, InputProvider provider) throws Exception {
        if (!validateArgs(args, 1, "укажите ключ. Использование: insert <key>")) return;
        String key = args[0];
        if (collectionManager.containsKey(key)) {
            System.out.println("Ключ уже существует. Используйте update или replace_if_lowe.");
            return;
        }

        Flat flat = readFlat(provider);
        flat.setId(collectionManager.getNextId());
        flat.setCreationDate(new Date());
        collectionManager.insert(key, flat);
        System.out.println("Элемент добавлен.");
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "добавить новый элемент с заданным ключом";
    }
}