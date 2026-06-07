package client;

import common.commands.*;
import common.models.Flat;
import common.models.View;
import java.io.IOException;
import java.util.*;

public class CommandParser {
    private final InputProvider provider;
    private final String login;
    private final String password;

    private static final Map<String, CommandFactory> COMMANDS = new LinkedHashMap<>();
    private static final List<Command> HELP_COMMANDS = Arrays.asList(
            new HelpCommand(), new InfoCommand(), new ShowCommand(),
            new InsertCommand(), new UpdateCommand(),
            new RemoveKeyCommand(null), new ClearCommand(),
            new RemoveLowerCommand(), new HistoryCommand(),
            new ReplaceIfLowerCommand(), new MinByCreationDateCommand(),
            new CountByViewCommand(null), new PrintAscendingCommand(),
            new ExitCommand()
    );

    static {
        COMMANDS.put("help", (args, p) -> new HelpCommand());
        COMMANDS.put("info", (args, p) -> new InfoCommand());
        COMMANDS.put("show", (args, p) -> new ShowCommand());
        COMMANDS.put("clear", (args, p) -> new ClearCommand());
        COMMANDS.put("exit", (args, p) -> new ExitCommand());
        COMMANDS.put("history", (args, p) -> new HistoryCommand());
        COMMANDS.put("min_by_creation_date", (args, p) -> new MinByCreationDateCommand());
        COMMANDS.put("print_ascending", (args, p) -> new PrintAscendingCommand());

        COMMANDS.put("remove_key", (args, p) -> {
            if (args.length < 1) throw new IllegalArgumentException("Укажите ключ");
            RemoveKeyCommand cmd = new RemoveKeyCommand(null);
            cmd.setKey(args[0]);
            return cmd;
        });

        COMMANDS.put("count_by_view", (args, p) -> {
            if (args.length < 1) throw new IllegalArgumentException("Укажите view");
            CountByViewCommand cmd = new CountByViewCommand(null);
            cmd.setView(View.valueOf(args[0].toUpperCase()));
            return cmd;
        });

        COMMANDS.put("insert", (args, p) -> {
            if (args.length < 1) throw new IllegalArgumentException("Укажите ключ");
            InsertCommand cmd = new InsertCommand();
            cmd.setKey(args[0]);
            cmd.setFlat(new ObjectReader(p).readFlat());
            return cmd;
        });

        COMMANDS.put("update", (args, p) -> {
            if (args.length < 1) throw new IllegalArgumentException("Укажите id");
            UpdateCommand cmd = new UpdateCommand();
            cmd.setId(Integer.parseInt(args[0]));
            cmd.setFlat(new ObjectReader(p).readFlat());
            return cmd;
        });

        COMMANDS.put("remove_lower", (args, p) -> {
            RemoveLowerCommand cmd = new RemoveLowerCommand();
            cmd.setFlat(new ObjectReader(p).readFlat());
            return cmd;
        });

        COMMANDS.put("replace_if_lower", (args, p) -> {
            if (args.length < 1) throw new IllegalArgumentException("Укажите ключ");
            ReplaceIfLowerCommand cmd = new ReplaceIfLowerCommand();
            cmd.setKey(args[0]);
            cmd.setFlat(new ObjectReader(p).readFlat());
            return cmd;
        });
    }

    @FunctionalInterface
    private interface CommandFactory {
        Command create(String[] args, InputProvider provider) throws IOException;
    }

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

        CommandFactory factory = COMMANDS.get(cmdName);
        if (factory == null) {
            throw new IllegalArgumentException("Неизвестная команда: " + cmdName);
        }
        Command cmd = factory.create(args, provider);
        if (cmd != null) {
            cmd.setCredentials(login, password);
        }
        return cmd;
    }

    public static List<String> getHelpInfo() {
        List<String> lines = new ArrayList<>();
        lines.add("Доступные команды:");
        for (Command cmd : HELP_COMMANDS) {
            String line = String.format("  %-25s %-8s – %s", cmd.getName(), cmd.getArgs(), cmd.getDescription());
            lines.add(line);
        }
        return lines;
    }
}