package common.commands;

import common.Response;
import common.models.Flat;
import server.CollectionManager;

import java.sql.SQLException;
import java.util.List;

public class RemoveLowerCommand extends Command {
    private static final long serialVersionUID = 1L;
    private final Flat flat;

    public RemoveLowerCommand(Flat flat) {
        this.flat = flat;
    }

    @Override
    public Response execute(CollectionManager cm) {
        try {
            List<Integer> deletedIds = cm.getDatabaseManager().deleteLowerFlats(flat, getLogin());
            for (int id : deletedIds) {
                String key = cm.getKeyById(id);
                if (key != null) cm.removeByKey(key);
            }
            return new Response("Удалено " + deletedIds.size() + " объект(ов), меньших заданного.");
        } catch (SQLException e) {
            return new Response("Ошибка БД: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "remove_lower";
    }
}
