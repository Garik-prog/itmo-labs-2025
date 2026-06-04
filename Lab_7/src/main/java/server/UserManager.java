package server;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserManager {
    private final DatabaseManager db;

    public UserManager(DatabaseManager db) {
        this.db = db;
    }

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean register(String login, String password) {
        return db.registerUser(login, md5(password));
    }

    public boolean authenticate(String login, String password) {
        return db.authenticateUser(login, md5(password));
    }
}
