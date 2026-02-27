package console;

import models.*;

import java.io.IOException;

/**
 * Класс для интерактивного чтения объектов Flat и связанных с ними классов
 * из источника ввода. Обеспечивает валидацию вводимых данных и повтор ввода
 * при ошибках.
 *
 * @see InputProvider
 * @see Flat
 */
public class ObjectReader {

    /** Источник ввода для чтения данных */
    private final InputProvider provider;

    /**
     * Создаёт ObjectReader с заданным источником ввода.
     *
     * @param provider источник ввода
     */
    public ObjectReader(InputProvider provider) {
        this.provider = provider;
    }

    /**
     * Читает объект Flat из источника ввода.
     * Последовательно запрашивает все поля с валидацией.
     *
     * @return созданный объект Flat
     * @throws IOException если произошла ошибка ввода
     */
    public Flat readFlat() throws IOException {
        Flat flat = new Flat();

        // Ввод name (не пустое)
        while (true) {
            System.out.print("Введите name (не пустая строка): ");
            String name = provider.readLine();
            if (name == null) throw new IOException("Неожиданный конец ввода");
            if (name.trim().isEmpty()) {
                System.out.println("Имя не может быть пустым.");
                continue;
            }
            flat.setName(name);
            break;
        }

        System.out.println("Введите координаты:");
        flat.setCoordinates(readCoordinates());

        // Ввод area (может быть null)
        while (true) {
            System.out.print("Введите area (целое число >0, или пустая строка для null): ");
            String line = provider.readLine();
            if (line == null) throw new IOException("Неожиданный конец ввода");
            if (line.trim().isEmpty()) {
                flat.setArea(null);
                break;
            }
            try {
                long area = Long.parseLong(line);
                if (area <= 0) throw new NumberFormatException();
                flat.setArea(area);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое положительное число.");
            }
        }

        // Ввод numberOfRooms (обязательное положительное)
        while (true) {
            System.out.print("Введите numberOfRooms (целое число >0): ");
            String line = provider.readLine();
            if (line == null) throw new IOException("Неожиданный конец ввода");
            try {
                int rooms = Integer.parseInt(line);
                if (rooms <= 0) throw new NumberFormatException();
                flat.setNumberOfRooms(rooms);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое положительное число.");
            }
        }


        // Ввод перечислений (могут быть null)
        flat.setFurnish(readEnum(Furnish.class, "furnish (возможные значения: DESIGNER, NONE, FINE, BAD, LITTLE; пустая строка для null)"));
        flat.setView(readEnum(View.class, "view (BAD, NORMAL, GOOD; пустая строка для null)"));
        flat.setTransport(readEnum(Transport.class, "transport (FEW, NONE, LITTLE, NORMAL, ENOUGH; пустая строка для null)"));

        // Ввод дома
        System.out.println("Введите дом:");
        flat.setHouse(readHouse());

        return flat;
    }

    /**
     * Читает объект Coordinates из источника ввода.
     *
     * @return созданный объект Coordinates
     * @throws IOException если произошла ошибка ввода
     */
    private Coordinates readCoordinates() throws IOException {
        Coordinates c = new Coordinates();

        // Ввод x (должно быть > -728)
        while (true) {
            System.out.print("Введите x (целое число > -728): ");
            String line = provider.readLine();
            if (line == null) throw new IOException("Неожиданный конец ввода");
            try {
                int x = Integer.parseInt(line);
                if (x <= -728) throw new NumberFormatException();
                c.setX(x);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: x должно быть целым числом больше -728.");
            }
        }

        // Ввод y (не null)
        while (true) {
            System.out.print("Введите y (целое число, не null): ");
            String line = provider.readLine();
            if (line == null) throw new IOException("Неожиданный конец ввода");
            try {
                long y = Long.parseLong(line);
                c.setY(y);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число.");
            }
        }

        return c;
    }

    /**
     * Читает объект House из источника ввода.
     *
     * @return созданный объект House
     * @throws IOException если произошла ошибка ввода
     */
    private House readHouse() throws IOException {
        House h = new House();

        // Ввод названия дома (не пустое)
        while (true) {
            System.out.print("Введите название дома (не пустая строка): ");
            String name = provider.readLine();
            if (name == null) throw new IOException("Неожиданный конец ввода");
            if (name.trim().isEmpty()) {
                System.out.println("Название не может быть пустым.");
                continue;
            }
            h.setName(name);
            break;
        }

        // Ввод года (обязательный положительный)
        while (true) {
            System.out.print("Введите год постройки (целое число > 0): ");
            String line = provider.readLine();
            if (line == null) throw new IOException("Неожиданный конец ввода");
            try {
                int year = Integer.parseInt(line);
                if (year <= 0) throw new NumberFormatException();
                h.setYear(year);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое положительное число.");
            }
        }

        // Ввод количества квартир на этаже (может быть null)
        while (true) {
            System.out.print("Введите количество квартир на этаже (целое число > 0, или пустая строка для null): ");
            String line = provider.readLine();
            if (line == null) throw new IOException("Неожиданный конец ввода");
            if (line.trim().isEmpty()) {
                h.setNumberOfFlatsOnFloor(null);
                break;
            }
            try {
                int num = Integer.parseInt(line);
                if (num <= 0) throw new NumberFormatException();
                h.setNumberOfFlatsOnFloor(num);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое положительное число.");
            }
        }

        return h;
    }

    /**
     * Универсальный метод для чтения значения перечисления.
     * Пустая строка интерпретируется как null.
     *
     * @param <T> тип перечисления
     * @param enumClass класс перечисления
     * @param prompt подсказка для пользователя
     * @return константа перечисления или null
     * @throws IOException если произошла ошибка ввода
     */
    private <T extends Enum<T>> T readEnum(Class<T> enumClass, String prompt) throws IOException {
        while (true) {
            System.out.print("Введите " + prompt + ": ");
            String line = provider.readLine();
            if (line == null) throw new IOException("Неожиданный конец ввода");
            if (line.trim().isEmpty()) {
                return null;
            }
            try {
                return Enum.valueOf(enumClass, line.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: введите одно из допустимых значений.");
            }
        }
    }
}