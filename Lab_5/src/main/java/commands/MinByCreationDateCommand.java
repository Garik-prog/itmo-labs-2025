package commands;

import manager.CollectionManager;
import console.InputProvider;
import models.Flat;

import java.util.Optional;

/**
 * Команда для вывода элемента с минимальной датой создания.
 *
 * @see AbstractCommand
 */
public class MinByCreationDateCommand extends AbstractCommand {

    /**
     * Создаёт команду min_by_creation_date.
     *
     * @param collectionManager менеджер коллекции
     */
    public MinByCreationDateCommand(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /**
     * Выполняет команду min_by_creation_date.
     * Находит элемент с минимальной датой создания и выводит его.
     *
     * @param args аргументы команды (не используются)
     * @param provider источник ввода (не используется)
     */
    @Override
    public void execute(String[] args, InputProvider provider) {
        Optional<Flat> min = collectionManager.minByCreationDate();
        if (min.isPresent()) {
            System.out.println("Элемент с минимальной датой создания:");
            System.out.println(min.get());
        } else {
            System.out.println("Коллекция пуста.");
        }
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "вывести любой объект с минимальным creationDate";
    }
}