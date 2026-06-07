package client.gui;

import client.ClientNetwork;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static ClientNetwork network;
    private static String currentLogin;
    private static String currentPassword;

    @Override
    public void start(Stage primaryStage) {
        LoginController loginController = new LoginController(primaryStage);
        loginController.show();
    }

    @Override
    public void stop() {
        if (network != null) {
            try { network.close(); } catch (Exception ignored) {}
        }
    }

    public static ClientNetwork getNetwork() { return network; }
    public static void setNetwork(ClientNetwork n) { network = n; }

    public static String getCurrentLogin() { return currentLogin; }
    public static void setCurrentLogin(String login) { currentLogin = login; }

    public static String getCurrentPassword() { return currentPassword; }
    public static void setCurrentPassword(String password) { currentPassword = password; }
}
