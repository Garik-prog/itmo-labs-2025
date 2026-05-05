package common.commands;

import common.Response;
import common.models.Flat;
import server.CollectionManager;

public class ReplaceIfLowerCommand extends Command {
    private static final long serialVersionUID = 1L;
    private final String key;
    private final Flat newFlat;

    public ReplaceIfLowerCommand(String key, Flat newFlat) {
        this.key = key;
        this.newFlat = newFlat;
    }

    @Override
    public Response execute(CollectionManager cm) {
        Flat old = cm.get(key);
        if (old == null) {
            return new Response("Ключ '" + key + "' не найден.");
        }
        newFlat.setId(old.getId());
        newFlat.setCreationDate(old.getCreationDate());
        if (cm.replaceIfLower(key, newFlat)) {
            return new Response("Значение по ключу '" + key + "' заменено.");
        } else {
            return new Response("Новое значение не меньше старого. Замена не выполнена.");
        }
    }

    @Override
    public String getName() { return "replace_if_lower"; }
}