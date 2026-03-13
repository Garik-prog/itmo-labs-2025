package commands;

import manager.CollectionManager;
import console.InputProvider;
import models.Flat;

public class ReplaceIfLowerCommand extends AbstractCommand {

    public ReplaceIfLowerCommand(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute(String[] args, InputProvider provider) throws Exception {
        if (!validateArgs(args, 1, "укажите ключ. Использование: replace_if_lowe <key>")) return;
        String key = args[0];
        Flat oldFlat = collectionManager.get(key);
        if (oldFlat == null) {
            System.out.println("Ключ не найден.");
            return;
        }

        Flat newFlat = readFlat(provider);
        newFlat.setId(oldFlat.getId());
        newFlat.setCreationDate(oldFlat.getCreationDate());

        if (collectionManager.replaceIfLower(key, newFlat)) {
            System.out.println("Значение заменено.");
        } else {
            System.out.println("Новое значение не меньше старого. Замена не выполнена.");
        }
    }

    @Override
    public String getDescription() {
        return "заменить значение по ключу, если новое значение меньше старого";
    }
}