package commands;

import manager.CollectionManager;
import console.InputProvider;
import console.ObjectReader;
import models.Flat;

/**
 * Абстрактный базовый класс для команд, работающих с коллекцией.
 * Предоставляет общие методы для валидации аргументов и чтения объектов.
 */
public abstract class AbstractCommand implements Command {

    /** Менеджер коллекции, с которым работает команда */
    protected CollectionManager collectionManager;

    /**
     * Создаёт команду с заданным менеджером коллекции.
     *
     * @param collectionManager менеджер коллекции
     */
    public AbstractCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Проверяет, что количество переданных аргументов не меньше ожидаемого.
     *
     * @param args массив аргументов
     * @param expected ожидаемое минимальное количество аргументов
     * @param errorMessage сообщение об ошибке при нехватке аргументов
     * @return true если аргументов достаточно, иначе false
     */
    protected boolean validateArgs(String[] args, int expected, String errorMessage) {
        if (args.length < expected) {
            System.out.println("Ошибка: " + errorMessage);
            return false;
        }
        return true;
    }

    /**
     * Читает объект Flat из источника ввода (полное создание).
     *
     * @param provider источник ввода
     * @return созданный объект Flat
     * @throws Exception если произошла ошибка ввода
     */
    protected Flat readFlat(InputProvider provider) throws Exception {
        ObjectReader reader = new ObjectReader(provider);
        return reader.readFlat();
    }

    /**
     * Читает значение одного поля для patch-обновления.
     *
     * @param provider источник ввода
     * @param fieldNumber номер поля (1..11)
     * @param current текущее значение для подсказки
     * @return новое значение поля
     * @throws Exception если ошибка ввода
     */
    protected Object readFieldValue(InputProvider provider, int fieldNumber, Object current) throws Exception {
        ObjectReader reader = new ObjectReader(provider);
        return reader.readFieldValue(fieldNumber, current);
    }
}