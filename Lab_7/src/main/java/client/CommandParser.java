package client;

import common.commands.*;
import common.models.Flat;
import common.models.View;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            case "help": return new HelpCommand();
            case "info": return new InfoCommand();
            case "show": return new ShowCommand();
            case "insert":
                if (args.length < 1) throw new IllegalArgumentException("Укажите ключ");
                return new InsertCommand(args[0], readFlat());
            case "update":
                if (args.length < 1) throw new IllegalArgumentException("Укажите id");
                return new UpdateCommand(Integer.parseInt(args[0]), readFlat());
            case "remove_key":
                if (args.length < 1) throw new IllegalArgumentException("Укажите ключ");
                return new RemoveKeyCommand(args[0]);
            case "clear": return new ClearCommand();
            case "execute_script": return null;
            case "exit": return new ExitCommand();
            case "remove_lower": return new RemoveLowerCommand(readFlat());
            case "history": return new HistoryCommand();
            case "replace_if_lower":
                if (args.length < 1) throw new IllegalArgumentException("Укажите ключ");
                return new ReplaceIfLowerCommand(args[0], readFlat());
            case "min_by_creation_date": return new MinByCreationDateCommand();
            case "count_by_view":
                if (args.length < 1) throw new IllegalArgumentException("Укажите view");
                return new CountByViewCommand(View.valueOf(args[0].toUpperCase()));
            case "print_ascending": return new PrintAscendingCommand();
            case "save": throw new UnsupportedOperationException("Команда save недоступна — данные хранятся в БД.");
            default: throw new IllegalArgumentException("Неизвестная команда: " + cmdName);
        }
    }

    private Flat readFlat() throws IOException {
        return new ObjectReader(provider).readFlat();
    }

    public static List<String> getHelpInfo() {
        List<Command> commands = new ArrayList<>();
        commands.add(new HelpCommand());
        commands.add(new InfoCommand());
        commands.add(new ShowCommand());
        commands.add(new InsertCommand(null, null));
        commands.add(new UpdateCommand(0, null));
        commands.add(new RemoveKeyCommand(null));
        commands.add(new ClearCommand());
        commands.add(new RemoveLowerCommand(null));
        commands.add(new HistoryCommand());
        commands.add(new ReplaceIfLowerCommand(null, null));
        commands.add(new MinByCreationDateCommand());
        commands.add(new CountByViewCommand(null));
        commands.add(new PrintAscendingCommand());
        commands.add(new ExitCommand());
        List<String> lines = new ArrayList<>();
        lines.add("Доступные команды:");
        for (Command cmd : commands) {
            String line = String.format("  %-25s %-8s – %s", cmd.getName(), cmd.getArgs(), cmd.getDescription());
            lines.add(line);
        }
        return lines;
    }
}