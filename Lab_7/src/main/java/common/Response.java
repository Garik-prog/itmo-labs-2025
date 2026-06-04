package common;

import common.models.Flat;
import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String message;
    private final List<Flat> flats;
    private final boolean success;

    public Response(String message) {
        this(message, null, true);
    }

    public Response(String message, List<Flat> flats) {
        this(message, flats, true);
    }

    public Response(String message, boolean success) {
        this(message, null, success);
    }

    public Response(String message, List<Flat> flats, boolean success) {
        this.message = message;
        this.flats = flats;
        this.success = success;
    }

    public String message() { return message; }
    public List<Flat> flats() { return flats; }
    public boolean isSuccess() { return success; }
}