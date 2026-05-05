package common.commands;

import common.Response;
import server.CollectionManager;

public class HelpCommand extends Command {
    private static final long serialVersionUID = 1L;

    @Override
    public Response execute(CollectionManager cm) {
        // Клиент обрабатывает help локально, заглушка для сервера
        return new Response("Справка недоступна на сервере");
    }

    @Override
    public String getName() { return "help"; }
}