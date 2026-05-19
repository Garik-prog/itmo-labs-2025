package common.models;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 1L;
    private int x;
    private Long y;

    public Coordinates() {}

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public Long getY() { return y; }
    public void setY(Long y) { this.y = y; }

    @Override
    public String toString() { return "(" + x + ", " + y + ")"; }
}
