package common.models;

import java.io.Serializable;

public class House implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int year;
    private Integer numberOfFlatsOnFloor;

    public House() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public Integer getNumberOfFlatsOnFloor() { return numberOfFlatsOnFloor; }
    public void setNumberOfFlatsOnFloor(Integer numberOfFlatsOnFloor) { this.numberOfFlatsOnFloor = numberOfFlatsOnFloor; }

    @Override
    public String toString() {
        return String.format("'%s' %dг (%d эт)",
                name.length() > 10 ? name.substring(0, 8) + "..." : name,
                year,
                numberOfFlatsOnFloor != null ? numberOfFlatsOnFloor : 0);
    }
}