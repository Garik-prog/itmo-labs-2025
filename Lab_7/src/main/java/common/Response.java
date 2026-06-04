package common;

import common.models.Flat;
import java.io.Serializable;
import java.util.List;

public record Response(String message, List<Flat> flats, boolean success) implements Serializable {
    private static final long serialVersionUID = 1L;

    public Response(String message) {
        this(message, null, true);
    }

    public Response(String message, List<Flat> flats) {
        this(message, flats, true);
    }

    public Response(String message, boolean success) {
        this(message, null, success);
    }

    public boolean isSuccess() {
        return success;
    }
}