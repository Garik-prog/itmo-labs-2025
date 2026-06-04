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
            flat.setName(name.trim());
            break;
        }

        System.out.println("Введите координаты:");
        flat.setCoordinates(readCoordinates());

        while (true) {
            System.out.print("Введите area (целое число >0): ");
            String line = provider.readLine();
            if (line == null) throw new IOException("Неожиданный конец ввода");
            try {
                long area = Long.parseLong(line.trim());
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
                int rooms = Integer.parseInt(line.trim());
                if (rooms <= 0) throw new NumberFormatException();
                flat.setNumberOfRooms(rooms);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое положительное число.");
            }
        }

        flat.setFurnish(readEnum(Furnish.class, "furnish (DESIGNER, NONE, FINE, BAD, LITTLE; пусто=null)"));
        flat.setView(readEnum(View.class, "view (BAD, NORMAL, GOOD; пусто=null)"));
        flat.setTransport(readEnum(Transport.class, "transport (FEW, NONE, LITTLE, NORMAL, ENOUGH; пусто=null)"));

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
                int x = Integer.parseInt(line.trim());
                if (x <= -728) throw new NumberFormatException();
                c.setX(x);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: x должно быть > -728.");
            }
        }
        while (true) {
            System.out.print("Введите y (целое число): ");
            String line = provider.readLine();
            if (line == null) throw new IOException("Неожиданный конец ввода");
            try {
                c.setY(Long.parseLong(line.trim()));
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
            System.out.print("Название дома (не пустое): ");
            String name = provider.readLine();
            if (name == null) throw new IOException("Неожиданный конец ввода");
            if (name.trim().isEmpty()) {
                System.out.println("Название не может быть пустым.");
                continue;
            }
            h.setName(name.trim());
            break;
        }
        while (true) {
            System.out.print("Год постройки (целое >0): ");
            String line = provider.readLine();
            if (line == null) throw new IOException("Неожиданный конец ввода");
            try {
                int year = Integer.parseInt(line.trim());
                if (year <= 0) throw new NumberFormatException();
                h.setYear(year);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое положительное число.");
            }
        }
        while (true) {
            System.out.print("Квартир на этаже (целое >0, или пусто=null): ");
            String line = provider.readLine();
            if (line == null) throw new IOException("Неожиданный конец ввода");
            if (line.trim().isEmpty()) {
                h.setNumberOfFlatsOnFloor(null);
                break;
            }
            try {
                int n = Integer.parseInt(line.trim());
                if (n <= 0) throw new NumberFormatException();
                h.setNumberOfFlatsOnFloor(n);
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
            if (line.trim().isEmpty()) return null;
            try {
                return Enum.valueOf(enumClass, line.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: недопустимое значение.");
            }
        }
    }
}
