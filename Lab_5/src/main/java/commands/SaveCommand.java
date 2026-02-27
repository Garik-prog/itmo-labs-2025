package commands;

import manager.CollectionManager;
import console.InputProvider;
import manager.FileManager;

/**
 * Команда для сохранения коллекции в файл.
 *
 * @see AbstractCommand
 * @see FileManager
 */
public class SaveCommand extends AbstractCommand {

    /** Менеджер файлов для сохранения данных */
    private final FileManager fileManager;

    /**
     * Создаёт команду save.
     *
     * @param collectionManager менеджер коллекции
     * @param fileManager менеджер файлов
     */
    public SaveCommand(CollectionManager collectionManager, FileManager fileManager) {
        super(collectionManager);
        this.fileManager = fileManager;
    }

    /**
     * Выполняет команду save – сохраняет коллекцию в файл.
     *
     * @param args аргументы команды (не используются)
     * @param provider источник ввода (не используется)
     * @throws Exception если произошла ошибка при сохранении
     */
    @Override
    public void execute(String[] args, InputProvider provider) throws Exception {
        fileManager.save(collectionManager.getCollection());
        System.out.println("Коллекция сохранена в файл.");
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "сохранить коллекцию в файл";
    }
}