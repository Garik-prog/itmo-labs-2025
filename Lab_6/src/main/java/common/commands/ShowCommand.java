package common.commands;

import common.Response;
import common.models.Flat;
import server.CollectionManager;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ShowCommand extends Command {
    private static final long serialVersionUID = 1L;

    @Override
    public Response execute(CollectionManager cm) {
        if (cm.getCollection().isEmpty()) {
            return new Response("Коллекция пуста.");
        }
        List<Flat> sorted = cm.getCollection().values().stream()
                .sorted(Comparator.comparing(Flat::getName))
                .collect(Collectors.toList());
        return new Response("Содержимое коллекции:", sorted);
    }

    @Override
    public String getName() { return "show"; }
}