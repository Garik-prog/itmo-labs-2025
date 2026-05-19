package common.commands;

import common.Response;
import common.models.Flat;
import server.CollectionManager;
import java.sql.SQLException;

public class ReplaceIfLowerCommand extends Command {
    private static final long serialVersionUID = 1L;
    private final String key;
    private final Flat newFlat;

    public ReplaceIfLowerCommand(String key, Flat newFlat) {
        this.key = key;
        this.newFlat = newFlat;
    }

    @Override
    public Response execute(CollectionManager cm) {
        Flat old = cm.get(key);
        if (old == null) return new Response("Ключ '" + key + "' не найден.");
        if (!getLogin().equals(old.getOwnerLogin())) {
            return new Response("Нет прав: объект принадлежит пользователю " + old.getOwnerLogin() + ".");
        }
        if (newFlat.compareTo(old) < 0) {
            newFlat.setId(old.getId());
            newFlat.setCreationDate(old.getCreationDate());
            newFlat.setOwnerLogin(getLogin());
            try {
                boolean updated = cm.getDatabaseManager().updateFlat(old.getId(), newFlat, getLogin());
                if (!updated) return new Response("Ошибка обновления в БД.");
                cm.updateByKey(key, newFlat);
                return new Response("Значение по ключу '" + key + "' заменено.");
            } catch (SQLException e) {
                return new Response("Ошибка БД: " + e.getMessage());
            }
        }
        return new Response("Новое значение не меньше старого. Замена не выполнена.");
    }

    @Override
    public String getName() { return "replace_if_lower"; }
}
