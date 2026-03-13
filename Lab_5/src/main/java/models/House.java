package models;

/**
 * Класс, представляющий дом, в котором находится квартира.
 * Содержит название, год постройки и количество квартир на этаже.
 */
public class House {

    /** Название дома (не может быть null) */
    private String name;

    /** Год постройки (должен быть > 0) */
    private int year;

    /** Количество квартир на этаже (должно быть > 0, может быть null) */
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
     * @param name новое название (не null)
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
     * @param year новый год (> 0)
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
     * @param numberOfFlatsOnFloor новое количество (> 0 или null)
     */
    public void setNumberOfFlatsOnFloor(Integer numberOfFlatsOnFloor) {
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
    }

    /**
     * Возвращает строковое представление дома.
     *
     * @return строка вида "'название' год (этажей)"
     */
    @Override
    public String toString() {
        return String.format("'%s' %dг (%d эт)",
                name.length() > 10 ? name.substring(0, 8) + "..." : name,
                year,
                numberOfFlatsOnFloor != null ? numberOfFlatsOnFloor : 0);
    }
}