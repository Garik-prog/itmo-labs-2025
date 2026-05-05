package client;

import common.commands.*;
import common.models.*;

import java.io.IOException;
import java.util.Arrays;

public class CommandParser {
    private final InputProvider provider;

    public CommandParser(InputProvider provider) {
        this.provider = provider;
    }

    public Command parseCommand(String line) throws IOException {
        if (line == null || line.trim().isEmpty()) return null;
        String[] parts = line.trim().split("\\s+");
        String cmdName = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        switch (cmdName) {
            case "help":
                return new HelpCommand();
            case "info":
                return new InfoCommand();
            case "show":
                return new ShowCommand();
            case "insert":
                if (args.length < 1) throw new IllegalArgumentException("Укажите ключ");
                return new InsertCommand(args[0], readFlat());
            case "update":
                if (args.length < 1) throw new IllegalArgumentException("Укажите id");
                int id = Integer.parseInt(args[0]);
                return new UpdateCommand(id, readFlat());
            case "remove_key":
                if (args.length < 1) throw new IllegalArgumentException("Укажите ключ");
                return new RemoveKeyCommand(args[0]);
            case "clear":
                return new ClearCommand();
            case "save":
                throw new UnsupportedOperationException("Команда save недоступна с клиента");
            case "execute_script":
                return null;
            case "exit":
                return new ExitCommand();
            case "remove_lower":
                return new RemoveLowerCommand(readFlat());
            case "history":
                return new HistoryCommand();
            case "replace_if_lower":
                if (args.length < 1) throw new IllegalArgumentException("Укажите ключ");
                return new ReplaceIfLowerCommand(args[0], readFlat());
            case "min_by_creation_date":
                return new MinByCreationDateCommand();
            case "count_by_view":
                if (args.length < 1) throw new IllegalArgumentException("Укажите view");
                View view = View.valueOf(args[0].toUpperCase());
                return new CountByViewCommand(view);
            case "print_ascending":
                return new PrintAscendingCommand();
            default:
                throw new IllegalArgumentException("Неизвестная команда: " + cmdName);
        }
    }

    private Flat readFlat() throws IOException {
        ObjectReader reader = new ObjectReader(provider);
        return reader.readFlat();
    }
}