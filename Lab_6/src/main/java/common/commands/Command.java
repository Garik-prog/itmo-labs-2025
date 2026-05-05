package common.commands;

import common.Response;
import server.CollectionManager;
import java.io.Serializable;

public abstract class Command implements Serializable {
    private static final long serialVersionUID = 1L;

    public abstract Response execute(CollectionManager collectionManager);
    public abstract String getName();
}