package common.models;

import java.io.Serializable;
import java.util.Date;

public class Flat implements Comparable<Flat>, Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private Coordinates coordinates;
    private Date creationDate;
    private long area;
    private int numberOfRooms;
    private Furnish furnish;
    private View view;
    private Transport transport;
    private House house;
    private String ownerLogin;

    public Flat() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getArea() {
        return area;
    }

    public void setArea(long area) {
        this.area = area;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Furnish getFurnish() {
        return furnish;
    }

    public void setFurnish(Furnish furnish) {
        this.furnish = furnish;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    @Override
    public int compareTo(Flat o) {
        int cmp = Long.compare(this.area, o.area);
        if (cmp != 0) return cmp;
        cmp = Integer.compare(this.numberOfRooms, o.numberOfRooms);
        if (cmp != 0) return cmp;
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return String.format("Flat[%d] '%s' | Комнат:%d | Площадь:%d | Вид:%s | Дом:%s | Владелец:%s",
                id,
                name.length() > 15 ? name.substring(0, 12) + "..." : name,
                numberOfRooms,
                area,
                view != null ? view : "не указан",
                house != null ? house.toString() : "нет",
                ownerLogin != null ? ownerLogin : "?");
    }
}
