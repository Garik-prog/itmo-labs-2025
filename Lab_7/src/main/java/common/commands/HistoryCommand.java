package common.commands;

import common.Response;
import server.CollectionManager;
import java.util.List;
import java.util.StringJoiner;

public class HistoryCommand extends Command {
    private static final long serialVersionUID = 1L;

    @Override
    public Response execute(CollectionManager cm) {
        List<String> history = cm.getHistory();
        if (history.isEmpty()) return new Response("История пуста.");
        StringJoiner sj = new StringJoiner("\n", "Последние команды:\n", "");
        for (String cmd : history) sj.add(cmd);
        return new Response(sj.toString());
    }

    @Override
    public String getName() { return "history"; }
}
