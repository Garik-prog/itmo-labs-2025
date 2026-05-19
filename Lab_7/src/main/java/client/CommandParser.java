package client;

import common.commands.*;
import common.models.*;
import java.io.IOException;
import java.util.Arrays;

public class CommandParser {
    private final InputProvider provider;
    private final String login;
    private final String password;

    public CommandParser(InputProvider provider, String login, String password) {
        this.provider = provider;
        this.login = login;
        this.password = password;
    }

    public Command parseCommand(String line) throws IOException {
        if (line == null || line.trim().isEmpty()) return null;
        String[] parts = line.trim().split("\\s+");
        String cmdName = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        Command cmd = buildCommand(cmdName, args);
        if (cmd != null) cmd.setCredentials(login, password);
        return cmd;
    }

    private Command buildCommand(String cmdName, String[] args) throws IOException {
        switch (cmdName) {
            case "help":           return new HelpCommand();
            case "info":           return new InfoCommand();
            case "show":           return new ShowCommand();
            case "insert":
                if (args.length < 1) throw new IllegalArgumentException("Укажите ключ");
                return new InsertCommand(args[0], readFlat());
            case "update":
                if (args.length < 1) throw new IllegalArgumentException("Укажите id");
                return new UpdateCommand(Integer.parseInt(args[0]), readFlat());
            case "remove_key":
                if (args.length < 1) throw new IllegalArgumentException("Укажите ключ");
                return new RemoveKeyCommand(args[0]);
            case "clear":          return new ClearCommand();
            case "execute_script": return null;
            case "exit":           return new ExitCommand();
            case "remove_lower":   return new RemoveLowerCommand(readFlat());
            case "history":        return new HistoryCommand();
            case "replace_if_lower":
                if (args.length < 1) throw new IllegalArgumentException("Укажите ключ");
                return new ReplaceIfLowerCommand(args[0], readFlat());
            case "min_by_creation_date": return new MinByCreationDateCommand();
            case "count_by_view":
                if (args.length < 1) throw new IllegalArgumentException("Укажите view");
                return new CountByViewCommand(View.valueOf(args[0].toUpperCase()));
            case "print_ascending": return new PrintAscendingCommand();
            case "save":
                throw new UnsupportedOperationException("Команда save недоступна — данные хранятся в БД.");
            default:
                throw new IllegalArgumentException("Неизвестная команда: " + cmdName);
        }
    }

    private Flat readFlat() throws IOException {
        return new ObjectReader(provider).readFlat();
    }
}
