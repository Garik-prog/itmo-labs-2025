package common.commands;

import common.Response;
import common.models.Flat;
import server.CollectionManager;
import java.util.List;

public class PrintAscendingCommand extends Command {
    private static final long serialVersionUID = 1L;

    @Override
    public Response execute(CollectionManager cm) {
        List<Flat> sorted = cm.getSortedByNaturalOrder();
        if (sorted.isEmpty()) return new Response("Коллекция пуста.");
        return new Response("Элементы в порядке возрастания:", sorted);
    }

    @Override
    public String getName() { return "print_ascending"; }
}
