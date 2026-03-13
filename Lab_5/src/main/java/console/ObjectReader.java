package console;

import models.*;

import java.io.IOException;

/**
 * Класс для интерактивного чтения объектов Flat и связанных с ними классов
 * из источника ввода. Обеспечивает валидацию вводимых данных и повтор ввода
 * при ошибках.
 */
public class ObjectReader {
    private final InputProvider provider;

    /**
     * Создаёт ObjectReader с заданным источником ввода.
     *
     * @param provider источник ввода (консоль или файл)
     */
    public ObjectReader(InputProvider provider) {
        this.provider = provider;
    }

    /**
     * Читает объект Flat из источника ввода (полное создание).
     * Последовательно запрашивает все поля с валидацией.
     *
     * @return созданный объект Flat
     * @throws IOException если произошла ошибка ввода
     */
    public Flat readFlat() throws IOException {
        Flat flat = new Flat();

        // Ввод name
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

        // Ввод area (обязательное, >0)
        while (true) {
            System.out.print("Введите area (целое число >0): ");
            String line = provider.readLine();
            if (line == null) throw new IOException("Неожиданный конец ввода");
            try {
                long area = Long.parseLong(line);
                if (area <= 0) throw new NumberFormatException();
                flat.setArea(area);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое положительное число.");
            }
        }

        // Ввод numberOfRooms
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

        // Ввод furnish (может быть null)
        flat.setFurnish(readEnum(Furnish.class,
                "furnish (DESIGNER, NONE, FINE, BAD, LITTLE; пустая строка для null)"));

        // Ввод view (может быть null)
        flat.setView(readEnum(View.class,
                "view (BAD, NORMAL, GOOD; пустая строка для null)"));

        // Ввод transport (может быть null)
        flat.setTransport(readEnum(Transport.class,
                "transport (FEW, NONE, LITTLE, NORMAL, ENOUGH; пустая строка для null)"));

        // Ввод дома
        System.out.println("Введите дом:");
        flat.setHouse(readHouse());

        return flat;
    }

    /**
     * Читает объект Coordinates.
     *
     * @return созданный объект Coordinates
     * @throws IOException если ошибка ввода
     */
    private Coordinates readCoordinates() throws IOException {
        Coordinates c = new Coordinates();

        // Ввод x
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

        // Ввод y
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
     * Читает объект House.
     *
     * @return созданный объект House
     * @throws IOException если ошибка ввода
     */
    private House readHouse() throws IOException {
        House h = new House();

        // Ввод названия дома
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

        // Ввод года
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
            System.out.print("Введите количество квартир на этаже (целое >0, или пустая строка для null): ");
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
     *
     * @param <T> тип перечисления
     * @param enumClass класс перечисления
     * @param prompt подсказка для пользователя
     * @return константа перечисления или null
     * @throws IOException если ошибка ввода
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

    /**
     * Читает значение одного поля для patch-обновления.
     *
     * @param fieldNumber номер поля (1..11)
     * @param current текущее значение (для подсказки)
     * @return новое значение (преобразованное к нужному типу)
     * @throws IOException если ошибка ввода
     */
    public Object readFieldValue(int fieldNumber, Object current) throws IOException {
        switch (fieldNumber) {
            case 1: // name
                while (true) {
                    System.out.print("Введите name (не пустая строка) [" + current + "]: ");
                    String line = provider.readLine();
                    if (line == null) throw new IOException("Неожиданный конец ввода");
                    if (line.trim().isEmpty()) {
                        System.out.println("Имя не может быть пустым.");
                        continue;
                    }
                    return line;
                }
            case 2: // x
                while (true) {
                    System.out.print("Введите x (целое > -728) [" + current + "]: ");
                    String line = provider.readLine();
                    if (line == null) throw new IOException("Неожиданный конец ввода");
                    try {
                        int x = Integer.parseInt(line);
                        if (x <= -728) throw new NumberFormatException();
                        return x;
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: x должно быть целым > -728.");
                    }
                }
            case 3: // y
                while (true) {
                    System.out.print("Введите y (целое) [" + current + "]: ");
                    String line = provider.readLine();
                    if (line == null) throw new IOException("Неожиданный конец ввода");
                    try {
                        return Long.parseLong(line);
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: введите целое число.");
                    }
                }
            case 4: // area
                while (true) {
                    System.out.print("Введите area (целое >0) [" + current + "]: ");
                    String line = provider.readLine();
                    if (line == null) throw new IOException("Неожиданный конец ввода");
                    try {
                        long area = Long.parseLong(line);
                        if (area <= 0) throw new NumberFormatException();
                        return area;
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: введите целое положительное число.");
                    }
                }
            case 5: // numberOfRooms
                while (true) {
                    System.out.print("Введите numberOfRooms (целое >0) [" + current + "]: ");
                    String line = provider.readLine();
                    if (line == null) throw new IOException("Неожиданный конец ввода");
                    try {
                        int rooms = Integer.parseInt(line);
                        if (rooms <= 0) throw new NumberFormatException();
                        return rooms;
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: введите целое положительное число.");
                    }
                }
            case 6: // furnish
                while (true) {
                    System.out.print("Введите furnish (DESIGNER, NONE, FINE, BAD, LITTLE) или Enter для null [" + current + "]: ");
                    String line = provider.readLine();
                    if (line == null) throw new IOException("Неожиданный конец ввода");
                    if (line.trim().isEmpty()) return null;
                    try {
                        return Furnish.valueOf(line.trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Ошибка: допустимые значения: DESIGNER, NONE, FINE, BAD, LITTLE");
                    }
                }
            case 7: // view
                while (true) {
                    System.out.print("Введите view (BAD, NORMAL, GOOD) или Enter для null [" + current + "]: ");
                    String line = provider.readLine();
                    if (line == null) throw new IOException("Неожиданный конец ввода");
                    if (line.trim().isEmpty()) return null;
                    try {
                        return View.valueOf(line.trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Ошибка: допустимые значения: BAD, NORMAL, GOOD");
                    }
                }
            case 8: // transport
                while (true) {
                    System.out.print("Введите transport (FEW, NONE, LITTLE, NORMAL, ENOUGH) или Enter для null [" + current + "]: ");
                    String line = provider.readLine();
                    if (line == null) throw new IOException("Неожиданный конец ввода");
                    if (line.trim().isEmpty()) return null;
                    try {
                        return Transport.valueOf(line.trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Ошибка: допустимые значения: FEW, NONE, LITTLE, NORMAL, ENOUGH");
                    }
                }
            case 9: // house.name
                while (true) {
                    System.out.print("Введите название дома (не пустая строка) [" + current + "]: ");
                    String line = provider.readLine();
                    if (line == null) throw new IOException("Неожиданный конец ввода");
                    if (line.trim().isEmpty()) {
                        System.out.println("Название не может быть пустым.");
                        continue;
                    }
                    return line;
                }
            case 10: // house.year
                while (true) {
                    System.out.print("Введите год постройки (целое >0) [" + current + "]: ");
                    String line = provider.readLine();
                    if (line == null) throw new IOException("Неожиданный конец ввода");
                    try {
                        int year = Integer.parseInt(line);
                        if (year <= 0) throw new NumberFormatException();
                        return year;
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: введите целое положительное число.");
                    }
                }
            case 11: // house.numberOfFlatsOnFloor
                while (true) {
                    System.out.print("Введите количество квартир на этаже (целое >0, или Enter для null) [" + current + "]: ");
                    String line = provider.readLine();
                    if (line == null) throw new IOException("Неожиданный конец ввода");
                    if (line.trim().isEmpty()) return null;
                    try {
                        int num = Integer.parseInt(line);
                        if (num <= 0) throw new NumberFormatException();
                        return num;
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: введите целое положительное число.");
                    }
                }
            default:
                throw new IllegalArgumentException("Неверный номер поля: " + fieldNumber);
        }
    }
}