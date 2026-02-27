package console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Реализация InputProvider для чтения с консоли (System.in).
 * Использует BufferedReader для эффективного чтения.
 *
 * @see InputProvider
 */
public class ConsoleInputProvider implements InputProvider {

    /** Буферизированный читатель из стандартного потока ввода */
    private final BufferedReader reader;

    /**
     * Создаёт провайдер ввода, читающий с консоли.
     */
    public ConsoleInputProvider() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Читает строку из консоли.
     *
     * @return введённая пользователем строка
     * @throws IOException если произошла ошибка ввода
     */
    @Override
    public String readLine() throws IOException {
        return reader.readLine();
    }

    /**
     * Закрывает BufferedReader.
     *
     * @throws IOException если произошла ошибка при закрытии
     */
    @Override
    public void close() throws IOException {
        reader.close();
    }
}