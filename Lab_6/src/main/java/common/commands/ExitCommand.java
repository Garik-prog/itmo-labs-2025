package common.commands;

import common.Response;
import server.CollectionManager;

public class ExitCommand extends Command {
    private static final long serialVersionUID = 1L;

    @Override
    public Response execute(CollectionManager cm) {
        // Эта команда не должна отправляться на сервер, но реализуем заглушку
        return new Response("exit выполнен на клиенте");
    }

    @Override
    public String getName() { return "exit"; }
}