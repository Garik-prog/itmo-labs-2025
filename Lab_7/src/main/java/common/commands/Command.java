package common.commands;

import common.Response;
import server.CollectionManager;

import java.io.Serializable;

public abstract class Command implements Serializable {
    private static final long serialVersionUID = 1L;
    private String login;
    private String password;

    public void setCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public abstract Response execute(CollectionManager collectionManager);

    public abstract String getName();
}
