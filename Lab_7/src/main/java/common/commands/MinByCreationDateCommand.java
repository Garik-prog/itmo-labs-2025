package common.commands;

import common.Response;
import common.models.Flat;
import server.CollectionManager;

import java.util.Optional;

public class MinByCreationDateCommand extends Command {
    private static final long serialVersionUID = 1L;

    @Override
    public Response execute(CollectionManager cm) {
        Optional<Flat> min = cm.minByCreationDate();
        return min.map(f -> new Response("Элемент с минимальной датой создания:\n" + f))
                .orElse(new Response("Коллекция пуста."));
    }

    @Override
    public String getName() {
        return "min_by_creation_date";
    }
}
