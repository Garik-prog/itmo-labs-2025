package common;

import common.models.Flat;

import java.io.Serializable;
import java.util.List;

public record Response(String message, List<Flat> flats) implements Serializable {
    private static final long serialVersionUID = 1L;

    public Response(String message) {
        this(message, null);
    }

}
