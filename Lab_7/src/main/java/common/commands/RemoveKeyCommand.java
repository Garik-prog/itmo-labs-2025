package common.commands;

import common.Response;
import common.models.Flat;
import server.CollectionManager;

import java.sql.SQLException;

public class RemoveKeyCommand extends Command {
    private static final long serialVersionUID = 1L;
    private final String key;

    public RemoveKeyCommand(String key) {
        this.key = key;
    }

    @Override
    public Response execute(CollectionManager cm) {
        Flat flat = cm.get(key);
        if (flat == null) return new Response("Ключ '" + key + "' не найден.");
        if (!getLogin().equals(flat.getOwnerLogin())) {
            return new Response("Нет прав: объект принадлежит пользователю " + flat.getOwnerLogin() + ".");
        }
        try {
            cm.getDatabaseManager().deleteFlatByKey(key, getLogin());
            cm.removeByKey(key);
            return new Response("Элемент с ключом '" + key + "' удалён.");
        } catch (SQLException e) {
            return new Response("Ошибка БД: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "remove_key";
    }
}
