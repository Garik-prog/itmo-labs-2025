package models;

/**
 * Класс, представляющий координаты квартиры.
 *
 * @see Flat
 */
public class Coordinates {

    /** Координата X. Должна быть больше -728. */
    private int x;

    /** Координата Y. Не может быть null. */
    private Long y;

    /**
     * Конструктор по умолчанию.
     */
    public Coordinates() {
    }

    /**
     * Возвращает координату X.
     *
     * @return значение X
     */
    public int getX() {
        return x;
    }

    /**
     * Устанавливает координату X.
     *
     * @param x новое значение X
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Возвращает координату Y.
     *
     * @return значение Y
     */
    public Long getY() {
        return y;
    }

    /**
     * Устанавливает координату Y.
     *
     * @param y новое значение Y
     */
    public void setY(Long y) {
        this.y = y;
    }

    /**
     * Возвращает строковое представление координат.
     *
     * @return строка в формате "(x, y)"
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}