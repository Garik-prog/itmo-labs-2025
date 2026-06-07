package common.commands;

import common.Response;
import common.models.Flat;
import server.CollectionManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShowCommand extends Command {
    private static final long serialVersionUID = 1L;

    @Override
    public Response execute(CollectionManager cm) {
        LinkedHashMap<String, Flat> snapshot = cm.getCollectionSnapshot();
        if (snapshot.isEmpty()) return new Response("Коллекция пуста.", new ArrayList<>());

        List<Flat> flatsList = new ArrayList<>();
        for (Map.Entry<String, Flat> entry : snapshot.entrySet()) {
            Flat flat = entry.getValue();
            flat.setMapKey(entry.getKey());
            flatsList.add(flat);
        }
        return new Response("Содержимое коллекции:", flatsList);
    }

    @Override
    public String getName() { return "show"; }

    @Override
    public String getArgs() { return ""; }

    @Override
    public String getDescription() { return "показать все элементы"; }
}
