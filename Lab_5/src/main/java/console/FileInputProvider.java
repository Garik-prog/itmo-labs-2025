package console;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Реализация InputProvider для чтения из файла.
 * Использует Scanner для построчного чтения.
 *
 * @see InputProvider
 */
public class FileInputProvider implements InputProvider {

    /** Сканер для чтения из файла */
    private final Scanner scanner;

    /**
     * Создаёт провайдер ввода для указанного файла.
     *
     * @param filename имя файла для чтения
     * @throws FileNotFoundException если файл не найден
     */
    public FileInputProvider(String filename) throws FileNotFoundException {
        scanner = new Scanner(new File(filename));
    }

    /**
     * Читает следующую строку из файла.
     *
     * @return строка из файла или null, если достигнут конец
     */
    @Override
    public String readLine() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return null;
    }

    /**
     * Закрывает Scanner.
     */
    @Override
    public void close() {
        scanner.close();
    }
}