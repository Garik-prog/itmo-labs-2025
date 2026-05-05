package common.commands;

import common.Response;
import server.CollectionManager;

public class RemoveKeyCommand extends Command {
    private static final long serialVersionUID = 1L;
    private final String key;

    public RemoveKeyCommand(String key) {
        this.key = key;
    }

    @Override
    public Response execute(CollectionManager cm) {
        if (!cm.containsKey(key)) {
            return new Response("Ключ '" + key + "' не найден.");
        }
        cm.removeByKey(key);
        return new Response("Элемент с ключом '" + key + "' удалён.");
    }

    @Override
    public String getName() { return "remove_key"; }
}