package commands;

import manager.CollectionManager;
import console.InputProvider;
import models.*;

/**
 * Команда для обновления элемента коллекции по его id.
 * Реализует интерактивный patch-режим: пользователь может изменять поля по одному.
 */
public class UpdateCommand extends AbstractCommand {

    /**
     * Создаёт команду update.
     *
     * @param collectionManager менеджер коллекции
     */
    public UpdateCommand(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /**
     * Выполняет команду update.
     * Находит элемент по id, создаёт его копию и позволяет интерактивно редактировать поля.
     *
     * @param args аргументы команды (ожидается id)
     * @param provider источник ввода
     * @throws Exception если произошла ошибка
     */
    @Override
    public void execute(String[] args, InputProvider provider) throws Exception {
        if (!validateArgs(args, 1, "укажите id. Использование: update <id>")) return;
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: id должно быть целым числом.");
            return;
        }

        String key = collectionManager.getKeyById(id);
        if (key == null) {
            System.out.println("Элемент с таким id не найден.");
            return;
        }

        Flat oldFlat = collectionManager.get(key);
        Flat newFlat = copyFlat(oldFlat); // создаём глубокую копию

        while (true) {
            // Вывод меню
            System.out.println("\n=== РЕДАКТИРОВАНИЕ ЭЛЕМЕНТА (id=" + id + ") ===");
            System.out.println("0. Выйти и сохранить изменения");
            System.out.println("1. name: " + newFlat.getName());
            System.out.println("2. coordinates.x: " + newFlat.getCoordinates().getX());
            System.out.println("3. coordinates.y: " + newFlat.getCoordinates().getY());
            System.out.println("4. area: " + newFlat.getArea());
            System.out.println("5. numberOfRooms: " + newFlat.getNumberOfRooms());
            System.out.println("6. furnish: " + newFlat.getFurnish());
            System.out.println("7. view: " + newFlat.getView());
            System.out.println("8. transport: " + newFlat.getTransport());
            System.out.println("9. house.name: " + (newFlat.getHouse() != null ? newFlat.getHouse().getName() : "null"));
            System.out.println("10. house.year: " + (newFlat.getHouse() != null ? newFlat.getHouse().getYear() : "null"));
            System.out.println("11. house.numberOfFlatsOnFloor: " +
                               (newFlat.getHouse() != null ? newFlat.getHouse().getNumberOfFlatsOnFloor() : "null"));

            System.out.print("Выберите поле для изменения (0-11): ");
            String input = provider.readLine();
            if (input == null) {
                System.out.println("Отмена редактирования.");
                return;
            }
            int choice;
            try {
                choice = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число.");
                continue;
            }

            if (choice == 0) {
                // Сохраняем изменения
                collectionManager.updateByKey(key, newFlat);
                System.out.println("Изменения сохранены.");
                return;
            }

            if (choice < 1 || choice > 11) {
                System.out.println("Неверный номер. Введите от 0 до 11.");
                continue;
            }

            // Получаем текущее значение для подсказки
            Object current = getCurrentValue(newFlat, choice);

            // Запрашиваем новое значение через унаследованный метод readFieldValue
            Object newValue = readFieldValue(provider, choice, current);

            // Применяем изменение
            applyValue(newFlat, choice, newValue);
            System.out.println("Поле обновлено.");
        }
    }

    /**
     * Создаёт глубокую копию объекта Flat.
     *
     * @param original оригинальный объект
     * @return полная копия с новыми объектами Coordinates и House
     */
    private Flat copyFlat(Flat original) {
        Flat copy = new Flat();
        copy.setId(original.getId());
        copy.setName(original.getName());

        // Coordinates
        Coordinates coords = new Coordinates();
        coords.setX(original.getCoordinates().getX());
        coords.setY(original.getCoordinates().getY());
        copy.setCoordinates(coords);

        copy.setCreationDate(original.getCreationDate());
        copy.setArea(original.getArea()); // примитив, просто копируем значение
        copy.setNumberOfRooms(original.getNumberOfRooms());
        copy.setFurnish(original.getFurnish());
        copy.setView(original.getView());
        copy.setTransport(original.getTransport());

        // House
        if (original.getHouse() != null) {
            House house = new House();
            house.setName(original.getHouse().getName());
            house.setYear(original.getHouse().getYear());
            house.setNumberOfFlatsOnFloor(original.getHouse().getNumberOfFlatsOnFloor());
            copy.setHouse(house);
        } else {
            copy.setHouse(null);
        }

        return copy;
    }

    /**
     * Возвращает текущее значение поля по его номеру.
     *
     * @param flat объект квартиры
     * @param fieldNumber номер поля (1-11)
     * @return текущее значение (может быть null)
     */
    private Object getCurrentValue(Flat flat, int fieldNumber) {
        switch (fieldNumber) {
            case 1: return flat.getName();
            case 2: return flat.getCoordinates().getX();
            case 3: return flat.getCoordinates().getY();
            case 4: return flat.getArea(); // автобоксинг в Long
            case 5: return flat.getNumberOfRooms();
            case 6: return flat.getFurnish();
            case 7: return flat.getView();
            case 8: return flat.getTransport();
            case 9: return flat.getHouse() != null ? flat.getHouse().getName() : null;
            case 10: return flat.getHouse() != null ? flat.getHouse().getYear() : null;
            case 11: return flat.getHouse() != null ? flat.getHouse().getNumberOfFlatsOnFloor() : null;
            default: return null;
        }
    }

    /**
     * Применяет новое значение к указанному полю объекта.
     *
     * @param flat объект квартиры
     * @param fieldNumber номер поля
     * @param value новое значение
     */
    private void applyValue(Flat flat, int fieldNumber, Object value) {
        switch (fieldNumber) {
            case 1: flat.setName((String) value); break;
            case 2: flat.getCoordinates().setX((Integer) value); break;
            case 3: flat.getCoordinates().setY((Long) value); break;
            case 4: flat.setArea((Long) value); break; // автораспаковка в long
            case 5: flat.setNumberOfRooms((Integer) value); break;
            case 6: flat.setFurnish((Furnish) value); break;
            case 7: flat.setView((View) value); break;
            case 8: flat.setTransport((Transport) value); break;
            case 9:
                if (flat.getHouse() == null) flat.setHouse(new House());
                flat.getHouse().setName((String) value);
                break;
            case 10:
                if (flat.getHouse() == null) flat.setHouse(new House());
                flat.getHouse().setYear((Integer) value);
                break;
            case 11:
                if (flat.getHouse() == null) flat.setHouse(new House());
                flat.getHouse().setNumberOfFlatsOnFloor((Integer) value);
                break;
        }
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание
     */
    @Override
    public String getDescription() {
        return "обновить значение элемента по его id (интерактивный patch)";
    }
}