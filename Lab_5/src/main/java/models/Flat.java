package models;

import java.util.Date;

/**
 * Класс, представляющий квартиру в коллекции.
 * Хранит все характеристики квартиры и обеспечивает естественную сортировку.
 *
 * @see Coordinates
 * @see House
 * @see Furnish
 * @see View
 * @see Transport
 */
public class Flat implements Comparable<Flat> {

    /** Уникальный идентификатор квартиры (генерируется автоматически, > 0) */
    private int id;

    /** Название квартиры (не может быть null или пустым) */
    private String name;

    /** Координаты квартиры (не могут быть null) */
    private Coordinates coordinates;

    /** Дата создания записи (генерируется автоматически, не null) */
    private Date creationDate;

    /** Площадь квартиры в квадратных метрах (обязательно > 0) */
    private long area;

    /** Количество комнат (обязательно > 0) */
    private int numberOfRooms;

    /** Тип отделки (может быть null) */
    private Furnish furnish;

    /** Вид из окна (может быть null) */
    private View view;

    /** Доступность транспорта (может быть null) */
    private Transport transport;

    /** Дом, в котором находится квартира (не может быть null) */
    private House house;

    /**
     * Конструктор по умолчанию.
     */
    public Flat() {
    }

    /**
     * Возвращает идентификатор квартиры.
     *
     * @return id квартиры
     */
    public int getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор квартиры.
     *
     * @param id новый идентификатор
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Возвращает название квартиры.
     *
     * @return название квартиры
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название квартиры.
     *
     * @param name новое название (не может быть null или пустым)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает координаты квартиры.
     *
     * @return объект Coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Устанавливает координаты квартиры.
     *
     * @param coordinates новые координаты
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Возвращает дату создания записи.
     *
     * @return дата создания
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Устанавливает дату создания записи.
     *
     * @param creationDate новая дата
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Возвращает площадь квартиры.
     *
     * @return площадь (всегда > 0)
     */
    public long getArea() {
        return area;
    }

    /**
     * Устанавливает площадь квартиры.
     *
     * @param area новая площадь (должна быть > 0)
     */
    public void setArea(long area) {
        this.area = area;
    }

    /**
     * Возвращает количество комнат.
     *
     * @return количество комнат
     */
    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    /**
     * Устанавливает количество комнат.
     *
     * @param numberOfRooms новое количество (должно быть > 0)
     */
    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    /**
     * Возвращает тип отделки.
     *
     * @return тип отделки или null
     */
    public Furnish getFurnish() {
        return furnish;
    }

    /**
     * Устанавливает тип отделки.
     *
     * @param furnish новый тип отделки (может быть null)
     */
    public void setFurnish(Furnish furnish) {
        this.furnish = furnish;
    }

    /**
     * Возвращает вид из окна.
     *
     * @return вид или null
     */
    public View getView() {
        return view;
    }

    /**
     * Устанавливает вид из окна.
     *
     * @param view новый вид (может быть null)
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Возвращает доступность транспорта.
     *
     * @return транспорт или null
     */
    public Transport getTransport() {
        return transport;
    }

    /**
     * Устанавливает доступность транспорта.
     *
     * @param transport новый транспорт (может быть null)
     */
    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    /**
     * Возвращает дом, в котором находится квартира.
     *
     * @return объект House
     */
    public House getHouse() {
        return house;
    }

    /**
     * Устанавливает дом для квартиры.
     *
     * @param house новый дом
     */
    public void setHouse(House house) {
        this.house = house;
    }

    /**
     * Сравнивает текущую квартиру с другой для сортировки.
     * Порядок сравнения: площадь -> количество комнат -> название.
     *
     * @param o другая квартира
     * @return отрицательное, 0 или положительное число
     */
    @Override
    public int compareTo(Flat o) {
        int areaComp = Long.compare(this.area, o.area);
        if (areaComp != 0) return areaComp;

        int roomsComp = Integer.compare(this.numberOfRooms, o.numberOfRooms);
        if (roomsComp != 0) return roomsComp;

        return this.name.compareTo(o.name);
    }

    /**
     * Возвращает строковое представление квартиры для вывода.
     *
     * @return краткая информация о квартире
     */
    @Override
    public String toString() {
        return String.format("Flat[%d] '%s' | Комнат:%d | Площадь:%d | Вид:%s | Дом:%s",
                id,
                name.length() > 15 ? name.substring(0, 12) + "..." : name,
                numberOfRooms,
                area,
                view != null ? view : "не указан",
                house != null ? house.toString() : "нет");
    }
}