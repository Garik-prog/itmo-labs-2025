package common.commands;

import common.Response;
import server.CollectionManager;

public class ExitCommand extends Command {
    private static final long serialVersionUID = 1L;

    @Override
    public Response execute(CollectionManager cm) {
        return new Response("exit");
    }

    @Override
    public String getName() {
        return "exit";
    }
}
