package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileInputProvider implements InputProvider {
    private final Scanner scanner;

    public FileInputProvider(String filename) throws FileNotFoundException {
        scanner = new Scanner(new File(filename));
    }

    @Override
    public String readLine() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    @Override
    public void close() {
        scanner.close();
    }
}
