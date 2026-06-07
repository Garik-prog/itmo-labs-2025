package common.commands;

import common.Response;
import server.CollectionManager;

public class HelpCommand extends Command {
    private static final long serialVersionUID = 1L;

    @Override
    public Response execute(CollectionManager cm) {
        return new Response("Команда help обрабатывается на клиенте.");
    }

    @Override
    public String getName() { return "help"; }

    @Override
    public String getArgs() { return ""; }

    @Override
    public String getDescription() { return "справка"; }
}