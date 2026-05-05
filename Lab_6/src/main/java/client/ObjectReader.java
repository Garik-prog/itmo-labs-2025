package client;

import common.models.*;

import java.io.IOException;

public class ObjectReader {
    private final InputProvider provider;

    public ObjectReader(InputProvider provider) {
        this.provider = provider;
    }

    public Flat readFlat() throws IOException {
        Flat flat = new Flat();

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

        flat.setFurnish(readEnum(Furnish.class,
                "furnish (DESIGNER, NONE, FINE, BAD, LITTLE; пустая строка для null)"));
        flat.setView(readEnum(View.class,
                "view (BAD, NORMAL, GOOD; пустая строка для null)"));
        flat.setTransport(readEnum(Transport.class,
                "transport (FEW, NONE, LITTLE, NORMAL, ENOUGH; пустая строка для null)"));

        System.out.println("Введите дом:");
        flat.setHouse(readHouse());

        return flat;
    }

    private Coordinates readCoordinates() throws IOException {
        Coordinates c = new Coordinates();

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

    private House readHouse() throws IOException {
        House h = new House();

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