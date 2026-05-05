package common;

import common.models.Flat;
import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String message;
    private final List<Flat> flats;

    public Response(String message) {
        this.message = message;
        this.flats = null;
    }

    public Response(String message, List<Flat> flats) {
        this.message = message;
        this.flats = flats;
    }

    public String getMessage() { return message; }
    public List<Flat> getFlats() { return flats; }
}