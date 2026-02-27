package commands;

import manager.CollectionManager;
import console.InputProvider;
import models.Flat;

import java.util.List;

/**
 * Команда для вывода всех элементов в порядке возрастания.
 * Использует естественный порядок сортировки Flat.
 *
 * @see AbstractCommand
 */
public class PrintAscendingCommand extends AbstractCommand {

    /**
     * Создаёт команду print_ascending.
     *
     * @param collectionManager менеджер коллекции
     */
    public PrintAscendingCommand(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /**
     * Выполняет команду print_ascending.
     * Получает отсортированный список элементов и выводит каждый.
     *
     * @param args аргументы команды (не используются)
     * @param provider источник ввода (не используется)
     */
    @Override
    public void execute(String[] args, InputProvider provider) {
        List<Flat> sorted = collectionManager.getSorted();
        if (sorted.isEmpty()) {
            System.out.println("Коллекция пуста.");
            return;
        }
        System.out.println("Элементы в порядке возрастания:");
        for (Flat flat : sorted) {
            System.out.println(flat);
        }
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "вывести элементы в порядке возрастания";
    }
}