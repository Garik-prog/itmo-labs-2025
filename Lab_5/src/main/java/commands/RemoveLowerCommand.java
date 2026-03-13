package commands;

import manager.CollectionManager;
import console.InputProvider;
import models.Flat;

public class RemoveLowerCommand extends AbstractCommand {

    public RemoveLowerCommand(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute(String[] args, InputProvider provider) throws Exception {
        Flat flat = readFlat(provider);
        collectionManager.removeLower(flat);
        System.out.println("Элементы, меньшие заданного, удалены.");
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, меньшие, чем заданный";
    }
}