package models;

/**
 * Класс, представляющий координаты квартиры.
 * Содержит две координаты: x и y.
 */
public class Coordinates {

    /** Координата X (должна быть больше -728) */
    private int x;

    /** Координата Y (не может быть null) */
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
     * @param x новое значение (должно быть > -728)
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Возвращает координату Y.
     *
     * @return значение Y (не null)
     */
    public Long getY() {
        return y;
    }

    /**
     * Устанавливает координату Y.
     *
     * @param y новое значение (не null)
     */
    public void setY(Long y) {
        this.y = y;
    }

    /**
     * Возвращает строковое представление координат в формате (x, y).
     *
     * @return строка вида "(x, y)"
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}