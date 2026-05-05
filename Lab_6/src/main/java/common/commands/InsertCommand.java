package common.commands;

import common.Response;
import common.models.Flat;
import server.CollectionManager;
import java.util.Date;

public class InsertCommand extends Command {
    private static final long serialVersionUID = 1L;
    private final String key;
    private final Flat flat;

    public InsertCommand(String key, Flat flat) {
        this.key = key;
        this.flat = flat;
    }

    @Override
    public Response execute(CollectionManager cm) {
        if (cm.containsKey(key)) {
            return new Response("Ключ '" + key + "' уже существует.");
        }
        flat.setId(cm.getNextId());
        flat.setCreationDate(new Date());
        cm.insert(key, flat);
        return new Response("Элемент с ключом '" + key + "' добавлен.");
    }

    @Override
    public String getName() { return "insert"; }
}