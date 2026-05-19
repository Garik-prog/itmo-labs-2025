package client;

import java.io.IOException;

public interface InputProvider extends AutoCloseable {
    String readLine() throws IOException;
    void close() throws IOException;
}
