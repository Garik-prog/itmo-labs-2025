package manager;

import models.Flat;
import models.View;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Менеджер коллекции. Хранит объекты Flat в LinkedHashMap,
 * предоставляет методы для работы с коллекцией, включая поиск,
 * добавление, удаление, сортировку и подсчёт.
 *
 * @see Flat
 */
public class CollectionManager {

    /** Основное хранилище коллекции (ключ - строка, значение - Flat) */
    private LinkedHashMap<String, Flat> collection;

    /** Дата инициализации менеджера (не меняется при загрузке) */
    private final Date initializationDate;

    /**
     * Создаёт пустой менеджер коллекции с текущей датой инициализации.
     */
    public CollectionManager() {
        collection = new LinkedHashMap<>();
        initializationDate = new Date();
    }

    /**
     * Возвращает всю коллекцию.
     *
     * @return коллекция LinkedHashMap
     */
    public LinkedHashMap<String, Flat> getCollection() {
        return collection;
    }

    /**
     * Заменяет текущую коллекцию новой.
     *
     * @param collection новая коллекция
     */
    public void setCollection(LinkedHashMap<String, Flat> collection) {
        this.collection = collection;
    }

    /**
     * Возвращает дату инициализации менеджера.
     *
     * @return дата инициализации
     */
    public Date getInitializationDate() {
        return initializationDate;
    }

    /**
     * Генерирует следующий уникальный id.
     * Берёт максимальный id в коллекции и прибавляет 1.
     * Если коллекция пуста, возвращает 1.
     *
     * @return следующий id
     */
    public int getNextId() {
        return collection.values().stream()
                       .mapToInt(Flat::getId)
                       .max()
                       .orElse(0) + 1;
    }

    /**
     * Проверяет наличие ключа в коллекции.
     *
     * @param key ключ для проверки
     * @return true если ключ существует
     */
    public boolean containsKey(String key) {
        return collection.containsKey(key);
    }

    /**
     * Возвращает объект Flat по ключу.
     *
     * @param key ключ элемента
     * @return объект Flat или null, если ключ не найден
     */
    public Flat get(String key) {
        return collection.get(key);
    }

    /**
     * Добавляет новый элемент в коллекцию.
     *
     * @param key ключ
     * @param flat объект Flat
     */
    public void insert(String key, Flat flat) {
        collection.put(key, flat);
    }

    /**
     * Обновляет существующий элемент по ключу.
     *
     * @param key ключ
     * @param flat новый объект Flat
     */
    public void updateByKey(String key, Flat flat) {
        collection.put(key, flat);
    }

    /**
     * Удаляет элемент по ключу.
     *
     * @param key ключ удаляемого элемента
     */
    public void removeByKey(String key) {
        collection.remove(key);
    }

    /**
     * Очищает коллекцию (удаляет все элементы).
     */
    public void clear() {
        collection.clear();
    }

    /**
     * Находит ключ элемента по его id.
     *
     * @param id искомый id
     * @return ключ элемента или null, если элемент не найден
     */
    public String getKeyById(int id) {
        for (Map.Entry<String, Flat> entry : collection.entrySet()) {
            if (entry.getValue().getId() == id) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Удаляет все элементы, которые меньше заданного.
     * Сравнение выполняется по методу compareTo класса Flat.
     *
     * @param flat эталонный объект для сравнения
     */
    public void removeLower(Flat flat) {
        collection.entrySet().removeIf(entry -> entry.getValue().compareTo(flat) < 0);
    }

    /**
     * Заменяет элемент по ключу, если новый элемент меньше старого.
     * Сохраняет id и дату создания старого элемента.
     *
     * @param key ключ элемента
     * @param newFlat новый объект Flat
     * @return true если замена выполнена, false если новый не меньше
     */
    public boolean replaceIfLower(String key, Flat newFlat) {
        Flat old = collection.get(key);
        if (old == null) return false;
        if (newFlat.compareTo(old) < 0) {
            collection.put(key, newFlat);
            return true;
        }
        return false;
    }

    /**
     * Находит элемент с минимальной датой создания.
     *
     * @return Optional с найденным элементом или пустой Optional, если коллекция пуста
     */
    public Optional<Flat> minByCreationDate() {
        return collection.values().stream()
                .min(Comparator.comparing(Flat::getCreationDate));
    }

    /**
     * Подсчитывает количество элементов с заданным значением view.
     *
     * @param view значение view для подсчёта
     * @return количество элементов
     */
    public long countByView(View view) {
        return collection.values().stream()
                .filter(flat -> flat.getView() == view)
                .count();
    }

    /**
     * Возвращает все элементы коллекции, отсортированные в естественном порядке.
     *
     * @return список отсортированных элементов
     */
    public List<Flat> getSorted() {
        return collection.values().stream()
                .sorted()
                .collect(Collectors.toList());
    }
}