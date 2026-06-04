package common.commands;

import common.Response;
import common.models.Flat;
import server.CollectionManager;
import java.sql.SQLException;
import java.util.Date;

public class InsertCommand extends Command {
    private static final long serialVersionUID = 1L;
    private String key;
    private Flat flat;

    public InsertCommand(String key, Flat flat) {
        this.key = key;
        this.flat = flat;
    }

    public InsertCommand() {

    }

    public void setKey(String key) { this.key = key; }
    public void setFlat(Flat flat) { this.flat = flat; }

    @Override
    public Response execute(CollectionManager cm) {
        if (cm.containsKey(key)) {
            return new Response("Ключ '" + key + "' уже существует.");
        }
        flat.setCreationDate(new Date());
        flat.setOwnerLogin(getLogin());
        try {
            int id = cm.getDatabaseManager().insertFlat(key, flat, getLogin());
            flat.setId(id);
            cm.insert(key, flat);
            return new Response("Элемент с ключом '" + key + "' добавлен (id=" + id + ").");
        } catch (SQLException e) {
            return new Response("Ошибка добавления в БД: " + e.getMessage());
        }
    }

    @Override
    public String getName() { return "insert"; }
    @Override
    public String getArgs() { return "<key>"; }
    @Override
    public String getDescription() { return "добавить элемент"; }
}