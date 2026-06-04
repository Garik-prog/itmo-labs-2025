package common.commands;

import common.Response;
import server.CollectionManager;

public class RegisterCommand extends Command {
    private static final long serialVersionUID = 1L;
    private final String newLogin;
    private final String newPassword;

    public RegisterCommand(String newLogin, String newPassword) {
        this.newLogin = newLogin;
        this.newPassword = newPassword;
    }

    @Override
    public Response execute(CollectionManager cm) {
        boolean ok = cm.getUserManager().register(newLogin, newPassword);
        return ok
                ? new Response("Регистрация прошла успешно. Теперь войдите в систему.")
                : new Response("Ошибка: пользователь с таким логином уже существует.");
    }

    @Override
    public String getName() {
        return "register";
    }
}
