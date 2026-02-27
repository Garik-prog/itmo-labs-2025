package models;

/**
 * Класс, представляющий дом, в котором находится квартира.
 *
 * @see Flat
 */
public class House {

    /** Название дома. Не может быть null. */
    private String name;

    /** Год постройки. Должен быть больше 0. */
    private int year;

    /** Количество квартир на этаже. Должно быть больше 0. Может быть null. */
    private Integer numberOfFlatsOnFloor;

    /**
     * Конструктор по умолчанию.
     */
    public House() {
    }

    /**
     * Возвращает название дома.
     *
     * @return название
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название дома.
     *
     * @param name новое название
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает год постройки.
     *
     * @return год
     */
    public int getYear() {
        return year;
    }

    /**
     * Устанавливает год постройки.
     *
     * @param year новый год
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Возвращает количество квартир на этаже.
     *
     * @return количество или null
     */
    public Integer getNumberOfFlatsOnFloor() {
        return numberOfFlatsOnFloor;
    }

    /**
     * Устанавливает количество квартир на этаже.
     *
     * @param numberOfFlatsOnFloor новое количество
     */
    public void setNumberOfFlatsOnFloor(Integer numberOfFlatsOnFloor) {
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
    }

    /**
     * Возвращает строковое представление дома.
     *
     * @return строка в формате "'название' год (этажей)"
     */
    @Override
    public String toString() {
        return String.format("'%s' %dг (%d эт)",
                name.length() > 10 ? name.substring(0, 8) + "..." : name,
                year,
                numberOfFlatsOnFloor != null ? numberOfFlatsOnFloor : 0);
    }
}