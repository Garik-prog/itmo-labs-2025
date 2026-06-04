package common.commands;

import common.Response;
import common.models.Flat;
import server.CollectionManager;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShowCommand extends Command {
    private static final long serialVersionUID = 1L;

    @Override
    public Response execute(CollectionManager cm) {
        LinkedHashMap<String, Flat> snapshot = cm.getCollectionSnapshot();
        if (snapshot.isEmpty()) return new Response("Коллекция пуста.");
        StringBuilder sb = new StringBuilder("Содержимое коллекции:\n");
        for (Map.Entry<String, Flat> entry : snapshot.entrySet()) {
            sb.append("Ключ: ").append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return new Response(sb.toString());
    }

    @Override
    public String getName() { return "show"; }

    @Override
    public String getArgs() { return ""; }

    @Override
    public String getDescription() { return "показать все элементы"; }
}