package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleInputProvider implements InputProvider {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public String readLine() throws IOException {
        return reader.readLine();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}