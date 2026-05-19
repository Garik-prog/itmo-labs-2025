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
        List<Flat> snapshot = new java.util.ArrayList<>(cm.getCollectionSnapshot().values());
        if (snapshot.isEmpty()) return new Response("Коллекция пуста.");
        List<Flat> sorted = snapshot.stream()
                .sorted(Comparator.comparing(Flat::getName))
                .collect(Collectors.toList());
        return new Response("Содержимое коллекции:", sorted);
    }

    @Override
    public String getName() { return "show"; }
}
