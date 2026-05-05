package common.commands;

import common.Response;
import server.CollectionManager;

public class ClearCommand extends Command {
    private static final long serialVersionUID = 1L;

    @Override
    public Response execute(CollectionManager cm) {
        cm.clear();
        return new Response("Коллекция очищена.");
    }

    @Override
    public String getName() { return "clear"; }
}