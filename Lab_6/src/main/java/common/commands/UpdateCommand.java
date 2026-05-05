package common.commands;

import common.Response;
import common.models.Flat;
import server.CollectionManager;

public class UpdateCommand extends Command {
    private static final long serialVersionUID = 1L;
    private final int id;
    private final Flat newFlat;

    public UpdateCommand(int id, Flat newFlat) {
        this.id = id;
        this.newFlat = newFlat;
    }

    @Override
    public Response execute(CollectionManager cm) {
        String key = cm.getKeyById(id);
        if (key == null) {
            return new Response("Элемент с id=" + id + " не найден.");
        }
        Flat old = cm.get(key);
        newFlat.setId(old.getId());
        newFlat.setCreationDate(old.getCreationDate());
        cm.updateByKey(key, newFlat);
        return new Response("Элемент с id=" + id + " обновлён.");
    }

    @Override
    public String getName() { return "update"; }
}