package common.commands;

import common.Response;
import server.CollectionManager;

public class InfoCommand extends Command {
    private static final long serialVersionUID = 1L;

    @Override
    public Response execute(CollectionManager cm) {
        String info = String.format(
                "Тип: LinkedHashMap<String, Flat>\n" +
                "Дата инициализации: %s\n" +
                "Количество элементов: %d",
                cm.getInitializationDate().toString(),
                cm.getCollection().size()
        );
        return new Response(info);
    }

    @Override
    public String getName() { return "info"; }
}