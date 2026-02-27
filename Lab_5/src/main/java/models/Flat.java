package models;

import java.util.Date;

/**
 * Основной класс коллекции. Хранит информацию о квартире.
 * Реализует интерфейс Comparable для естественной сортировки.
 *
 * @see Coordinates
 * @see House
 * @see Furnish
 * @see View
 * @see Transport
 */
public class Flat implements Comparable<Flat> {

    /** Уникальный идентификатор. Генерируется автоматически. */
    private int id;

    /** Название квартиры. Не может быть null или пустым. */
    private String name;

    /** Координаты квартиры. Не могут быть null. */
    private Coordinates coordinates;

    /** Дата создания. Генерируется автоматически. Не может быть null. */
    private Date creationDate;

    /** Площадь квартиры. Должна быть > 0. Может быть null. */
    private Long area;

    /** Количество комнат. Должно быть > 0. */
    private int numberOfRooms;

    /** Тип отделки. Может быть null. */
    private Furnish furnish;

    /** Вид из окна. Может быть null. */
    private View view;

    /** Доступность транспорта. Может быть null. */
    private Transport transport;

    /** Дом, в котором находится квартира. Не может быть null. */
    private House house;

    /**
     * Конструктор по умолчанию.
     */
    public Flat() {
    }

    /**
     * Возвращает id квартиры.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Устанавливает id квартиры.
     *
     * @param id новый id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Возвращает название квартиры.
     *
     * @return название
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название квартиры.
     *
     * @param name новое название
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает координаты квартиры.
     *
     * @return координаты
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
     * @return площадь или null
     */
    public Long getArea() {
        return area;
    }

    /**
     * Устанавливает площадь квартиры.
     *
     * @param area новая площадь
     */
    public void setArea(Long area) {
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
     * @param numberOfRooms новое количество
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
     * @param furnish новый тип
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
     * @param view новый вид
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
     * @param transport новый транспорт
     */
    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    /**
     * Возвращает дом квартиры.
     *
     * @return дом
     */
    public House getHouse() {
        return house;
    }

    /**
     * Устанавливает дом квартиры.
     *
     * @param house новый дом
     */
    public void setHouse(House house) {
        this.house = house;
    }

    /**
     * Сравнивает текущую квартиру с другой для сортировки.
     * Порядок сравнения:
     * 1. Площадь (null считается меньше любого числа)
     * 2. Количество комнат
     * 3. Название
     *
     * @param o другая квартира
     * @return отрицательное число, если текущая меньше, положительное если больше, 0 если равны
     */
    @Override
    public int compareTo(Flat o) {
        // Сравнение по area (null обрабатываем отдельно)
        if (this.area == null && o.area == null) {
            // равны по area
        } else if (this.area == null) {
            return -1;
        } else if (o.area == null) {
            return 1;
        } else {
            int areaComp = this.area.compareTo(o.area);
            if (areaComp != 0) return areaComp;
        }

        // Сравнение по количеству комнат
        int roomsComp = Integer.compare(this.numberOfRooms, o.numberOfRooms);
        if (roomsComp != 0) return roomsComp;

        // Сравнение по имени
        return this.name.compareTo(o.name);
    }

    /**
     * Возвращает строковое представление квартиры для вывода.
     *
     * @return строковое представление
     */
    @Override
    public String toString() {
        return String.format("Flat[%d] '%s' | Комнат:%d | Площадь:%s | Вид:%s | Дом:%s",
                id,
                name.length() > 15 ? name.substring(0, 12) + "..." : name,
                numberOfRooms,
                area != null ? area + "м^2" : "не указано",
                view != null ? view : "не указан",
                house != null ? house.toString() : "нет");
    }
}