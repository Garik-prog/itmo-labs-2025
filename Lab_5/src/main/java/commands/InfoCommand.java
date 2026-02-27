package commands;

import manager.CollectionManager;
import console.InputProvider;
import models.Flat;

/**
 * Команда для вывода информации о коллекции.
 * Выводит тип коллекции, дату инициализации, количество элементов,
 * общее количество комнат и среднее количество комнат.
 *
 * @see Command
 * @see CollectionManager
 */
public class InfoCommand implements Command {

    /** Ширина рамки для красивого вывода */
    private static final int WIDTH = 60;

    /** Менеджер коллекции */
    private final CollectionManager collectionManager;

    /**
     * Создаёт команду info.
     *
     * @param collectionManager менеджер коллекции
     */
    public InfoCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Выводит информацию о коллекции в форматированном виде.
     *
     * @param args аргументы команды (не используются)
     * @param provider источник ввода (не используется)
     */
    @Override
    public void execute(String[] args, InputProvider provider) {
        printCentered("ИНФОРМАЦИЯ О КОЛЛЕКЦИИ");

        printLine("Тип: LinkedHashMap<String, Flat>");
        printLine("Дата инициализации: " + collectionManager.getInitializationDate());
        printLine("Количество элементов: " + collectionManager.getCollection().size());

        long totalRooms = collectionManager.getCollection().values().stream()
                .mapToInt(Flat::getNumberOfRooms).sum();
        double avgRooms = collectionManager.getCollection().isEmpty() ? 0 :
                (double) totalRooms / collectionManager.getCollection().size();

        printLine("Всего комнат: " + totalRooms);
        printLine(String.format("Среднее кол-во комнат: %.1f", avgRooms));
    }

    /**
     * Выводит текст, выровненный по центру в рамке.
     *
     * @param text текст для центрирования
     */
    private void printCentered(String text) {
        int padding = (WIDTH - 2 - text.length()) / 2;
        int extra = (WIDTH - 2 - text.length()) % 2;
        System.out.println("│" + " ".repeat(padding) + text + " ".repeat(padding + extra) + "│");
    }

    /**
     * Выводит строку текста в рамке, обрезая при необходимости.
     *
     * @param text текст для вывода
     */
    private void printLine(String text) {
        if (text.length() > WIDTH - 4) {
            text = text.substring(0, WIDTH - 7) + "...";
        }
        System.out.println("│ " + text + " ".repeat(WIDTH - 3 - text.length()) + "│");
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "вывести информацию о коллекции";
    }
}