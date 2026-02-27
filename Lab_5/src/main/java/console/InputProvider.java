package console;

import java.io.IOException;

/**
 * Интерфейс для абстракции источника ввода данных.
 * Позволяет единообразно работать с консолью и файлами.
 * Реализует AutoCloseable для использования в try-with-resources.
 *
 * @see ConsoleInputProvider
 * @see FileInputProvider
 */
public interface InputProvider extends AutoCloseable {

    /**
     * Читает следующую строку из источника ввода.
     *
     * @return прочитанная строка или null, если достигнут конец ввода
     * @throws IOException если произошла ошибка ввода
     */
    String readLine() throws IOException;

    /**
     * Закрывает источник ввода и освобождает связанные ресурсы.
     *
     * @throws IOException если произошла ошибка при закрытии
     */
    void close() throws IOException;
}