package common.commands;

import common.Response;
import common.models.Flat;
import server.CollectionManager;

public class RemoveLowerCommand extends Command {
    private static final long serialVersionUID = 1L;
    private final Flat flat;

    public RemoveLowerCommand(Flat flat) {
        this.flat = flat;
    }

    @Override
    public Response execute(CollectionManager cm) {
        cm.removeLower(flat);
        return new Response("Элементы, меньшие заданного, удалены.");
    }

    @Override
    public String getName() { return "remove_lower"; }
}