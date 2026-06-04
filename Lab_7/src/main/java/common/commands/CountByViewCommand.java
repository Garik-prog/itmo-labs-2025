package common.commands;

import common.Response;
import common.models.View;
import server.CollectionManager;

public class CountByViewCommand extends Command {
    private static final long serialVersionUID = 1L;
    private final View view;

    public CountByViewCommand(View view) {
        this.view = view;
    }

    @Override
    public Response execute(CollectionManager cm) {
        long count = cm.countByView(view);
        return new Response("Количество элементов с view " + view + ": " + count);
    }

    @Override
    public String getName() { return "count_by_view"; }

    @Override
    public String getArgs() { return "<view>"; }

    @Override
    public String getDescription() { return "количество по view"; }
}