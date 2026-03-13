package commands;

import manager.CollectionManager;
import console.InputProvider;
import models.Flat;

import java.util.Map;

/**
 * Команда для вывода всех элементов коллекции.
 * Отображает каждый элемент в форматированном виде с рамкой.
 *
 * @see Command
 * @see CollectionManager
 */
public class ShowCommand implements Command {

    /** Ширина рамки для красивого вывода */
    private static final int WIDTH = 60;

    /** Менеджер коллекции */
    private final CollectionManager collectionManager;

    /**
     * Создаёт команду show.
     *
     * @param collectionManager менеджер коллекции
     */
    public ShowCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Выводит все элементы коллекции в форматированном виде.
     * Если коллекция пуста, выводит соответствующее сообщение.
     *
     * @param args аргументы команды (не используются)
     * @param provider источник ввода (не используется)
     */
    @Override
    public void execute(String[] args, InputProvider provider) {
        if (collectionManager.getCollection().isEmpty()) {
            System.out.println("Коллекция пуста.");
            return;
        }

        printSeparator('┌', '┐');
        printCentered("СОДЕРЖИМОЕ КОЛЛЕКЦИИ");
        printSeparator('├', '┤');

        int count = 0;
        for (Map.Entry<String, Flat> entry : collectionManager.getCollection().entrySet()) {
            count++;
            Flat flat = entry.getValue();

            printLine(String.format("%d. Ключ: %s ID: %d",
                    count, "'" + entry.getKey() + "'", flat.getId()));
            printLine("Название: " + flat.getName());
            printLine(String.format("Координаты: (%d, %d)",
                    flat.getCoordinates().getX(), flat.getCoordinates().getY()));

            String area = flat.getArea() + " м²";
            String date = String.format("%02d.%02d.%d",
                    flat.getCreationDate().getDate(),
                    flat.getCreationDate().getMonth() + 1,
                    flat.getCreationDate().getYear() + 1900);
            printLine(String.format("Комнат: %d  Площадь: %s  Дата: %s",
                    flat.getNumberOfRooms(), area, date));

            String furnish = flat.getFurnish() != null ? flat.getFurnish().toString() : "---";
            String view = flat.getView() != null ? flat.getView().toString() : "---";
            String transport = flat.getTransport() != null ? flat.getTransport().toString() : "---";
            printLine(String.format("Отделка: %s  Вид: %s  Транспорт: %s",
                    furnish, view, transport));

            if (flat.getHouse() != null) {
                printLine(String.format("Дом: %s (%d г., %d эт.)",
                        flat.getHouse().getName(),
                        flat.getHouse().getYear(),
                        flat.getHouse().getNumberOfFlatsOnFloor()));
            } else {
                printLine("Дом: ---");
            }

            if (count < collectionManager.getCollection().size()) {
                printSeparator('├', '┤');
            }
        }
        printSeparator('└', '┘');
        System.out.println("Всего элементов: " + collectionManager.getCollection().size());
    }

    /**
     * Выводит горизонтальную линию рамки с заданными символами углов.
     *
     * @param left левый символ рамки
     * @param right правый символ рамки
     */
    private void printSeparator(char left, char right) {
        System.out.println(left + "─".repeat(WIDTH - 2) + right);
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
        return "вывести все элементы коллекции";
    }
}