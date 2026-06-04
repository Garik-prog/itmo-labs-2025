package common.commands;

import common.Response;
import common.models.Flat;
import server.CollectionManager;
import java.sql.SQLException;

public class UpdateCommand extends Command {
    private static final long serialVersionUID = 1L;
    private final int id;
    private final Flat newFlat;

    public UpdateCommand(int id, Flat newFlat) {
        this.id = id;
        this.newFlat = newFlat;
    }

    @Override
    public Response execute(CollectionManager cm) {
        String key = cm.getKeyById(id);
        if (key == null) return new Response("Элемент с id=" + id + " не найден.");
        Flat old = cm.get(key);
        if (!getLogin().equals(old.getOwnerLogin())) {
            return new Response("Нет прав: объект принадлежит пользователю " + old.getOwnerLogin() + ".");
        }
        newFlat.setId(old.getId());
        newFlat.setCreationDate(old.getCreationDate());
        newFlat.setOwnerLogin(getLogin());
        try {
            boolean updated = cm.getDatabaseManager().updateFlat(id, newFlat, getLogin());
            if (!updated) return new Response("Ошибка обновления в БД (нет прав или запись не найдена).");
            cm.updateByKey(key, newFlat);
            return new Response("Элемент с id=" + id + " обновлён.");
        } catch (SQLException e) {
            return new Response("Ошибка БД: " + e.getMessage());
        }
    }

    @Override
    public String getName() { return "update"; }

    @Override
    public String getArgs() { return "<id>"; }

    @Override
    public String getDescription() { return "обновить элемент по id"; }
}