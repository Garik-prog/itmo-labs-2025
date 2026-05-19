package common.commands;

import common.Response;
import server.CollectionManager;
import java.sql.SQLException;

public class ClearCommand extends Command {
    private static final long serialVersionUID = 1L;

    @Override
    public Response execute(CollectionManager cm) {
        try {
            int removed = cm.getDatabaseManager().clearUserFlats(getLogin());
            cm.clearForUser(getLogin());
            return new Response("Удалено " + removed + " объект(ов), принадлежащих вам.");
        } catch (SQLException e) {
            return new Response("Ошибка БД: " + e.getMessage());
        }
    }

    @Override
    public String getName() { return "clear"; }
}
