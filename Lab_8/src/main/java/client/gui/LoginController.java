package client.gui;

import client.ClientNetwork;
import client.LocaleManager;
import common.Response;
import common.commands.InfoCommand;
import common.commands.RegisterCommand;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Locale;

public class LoginController {

    private final Stage stage;

    private Label titleLabel;
    private Label hostLabel;
    private Label portLabel;
    private Label userLabel;
    private Label passLabel;
    private TextField hostField;
    private TextField portField;
    private TextField userField;
    private PasswordField passField;
    private Button loginBtn;
    private Button registerBtn;
    private Button exitBtn;
    private Label errorLabel;
    private ComboBox<Locale> langBox;
    private Label langLabel;

    public LoginController(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        stage.setTitle(LocaleManager.get("login.title"));

        // Language selector
        langLabel = new Label(LocaleManager.get("main.language"));
        langBox = new ComboBox<>();
        langBox.getItems().addAll(LocaleManager.getSupportedLocales());
        langBox.setValue(LocaleManager.getLocale());
        langBox.setCellFactory(lv -> new LocaleListCell());
        langBox.setButtonCell(new LocaleListCell());
        langBox.setOnAction(e -> {
            Locale selected = langBox.getValue();
            if (selected != null) {
                LocaleManager.setLocale(selected);
                refreshLocale();
            }
        });

        HBox langRow = new HBox(8, langLabel, langBox);
        langRow.setAlignment(Pos.CENTER_RIGHT);

        // Title
        titleLabel = new Label(LocaleManager.get("login.welcome"));
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        // Connection fields
        hostLabel = new Label(LocaleManager.get("login.host"));
        hostField = new TextField("localhost");
        portLabel = new Label(LocaleManager.get("login.port"));
        portField = new TextField("5433");

        // Credentials
        userLabel = new Label(LocaleManager.get("login.username"));
        userField = new TextField();
        userField.setPromptText(LocaleManager.get("login.username"));
        passLabel = new Label(LocaleManager.get("login.password"));
        passField = new PasswordField();
        passField.setPromptText(LocaleManager.get("login.password"));

        // Buttons
        loginBtn = new Button(LocaleManager.get("login.btn.login"));
        registerBtn = new Button(LocaleManager.get("login.btn.register"));
        exitBtn = new Button(LocaleManager.get("login.btn.exit"));

        loginBtn.setDefaultButton(true);
        loginBtn.setPrefWidth(160);
        registerBtn.setPrefWidth(160);
        exitBtn.setPrefWidth(160);

        loginBtn.setStyle("-fx-background-color:#2980b9;-fx-text-fill:white;-fx-font-weight:bold;");
        registerBtn.setStyle("-fx-background-color:#27ae60;-fx-text-fill:white;-fx-font-weight:bold;");
        exitBtn.setStyle("-fx-background-color:#c0392b;-fx-text-fill:white;-fx-font-weight:bold;");

        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setWrapText(true);

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, hostLabel, hostField);
        grid.addRow(1, portLabel, portField);
        grid.addRow(2, new Separator(), new Separator());
        grid.addRow(3, userLabel, userField);
        grid.addRow(4, passLabel, passField);
        GridPane.setColumnSpan(grid.getChildren().get(4), 2); // separator spans 2 cols
        GridPane.setColumnSpan(grid.getChildren().get(5), 2);

        VBox btnBox = new VBox(8, loginBtn, registerBtn, exitBtn);
        btnBox.setAlignment(Pos.CENTER);

        VBox card = new VBox(18, langRow, titleLabel, grid, errorLabel, btnBox);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color:white;-fx-border-radius:8;-fx-background-radius:8;"
                + "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.2),12,0,0,4);");
        card.setMaxWidth(380);

        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color:#ecf0f1;");
        root.setPadding(new Insets(40));

        loginBtn.setOnAction(e -> handleLogin(false));
        registerBtn.setOnAction(e -> handleLogin(true));
        exitBtn.setOnAction(e -> Platform.exit());

        Scene scene = new Scene(root, 480, 550);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void handleLogin(boolean isRegister) {
        String host = hostField.getText().trim();
        String portStr = portField.getText().trim();
        String login = userField.getText().trim();
        String password = passField.getText();

        if (login.isEmpty() || password.isEmpty()) {
            errorLabel.setText(LocaleManager.get("login.err.empty"));
            return;
        }

        int port;
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            errorLabel.setText(LocaleManager.get("login.err.connection"));
            return;
        }

        loginBtn.setDisable(true);
        registerBtn.setDisable(true);
        errorLabel.setText("");

        final String finalHost = host;
        final int finalPort = port;
        final String finalLogin = login;
        final String finalPassword = password;

        new Thread(() -> {
            try {
                ClientNetwork net = new ClientNetwork(finalHost, finalPort);

                if (isRegister) {
                    RegisterCommand reg = new RegisterCommand(finalLogin, finalPassword);
                    Response r = net.sendCommand(reg);
                    if (!r.isSuccess() || !r.message().contains("успешно")) {
                        Platform.runLater(() -> {
                            errorLabel.setText(r.message());
                            loginBtn.setDisable(false);
                            registerBtn.setDisable(false);
                        });
                        return;
                    }
                }

                InfoCommand info = new InfoCommand();
                info.setSkipHistory(true);
                info.setCredentials(finalLogin, finalPassword);
                Response r = net.sendCommand(info);

                if (!r.isSuccess()) {
                    net.close();
                    Platform.runLater(() -> {
                        errorLabel.setText(LocaleManager.get("login.err.credentials"));
                        loginBtn.setDisable(false);
                        registerBtn.setDisable(false);
                    });
                    return;
                }

                MainApp.setNetwork(net);
                MainApp.setCurrentLogin(finalLogin);
                MainApp.setCurrentPassword(finalPassword);

                Platform.runLater(() -> {
                    MainController mainController = new MainController(stage);
                    mainController.show();
                });

            } catch (Exception ex) {
                Platform.runLater(() -> {
                    errorLabel.setText(LocaleManager.get("login.err.connection") + ": " + ex.getMessage());
                    loginBtn.setDisable(false);
                    registerBtn.setDisable(false);
                });
            }
        }, "login-thread").start();
    }

    private void refreshLocale() {
        stage.setTitle(LocaleManager.get("login.title"));
        titleLabel.setText(LocaleManager.get("login.welcome"));
        hostLabel.setText(LocaleManager.get("login.host"));
        portLabel.setText(LocaleManager.get("login.port"));
        userLabel.setText(LocaleManager.get("login.username"));
        passLabel.setText(LocaleManager.get("login.password"));
        userField.setPromptText(LocaleManager.get("login.username"));
        passField.setPromptText(LocaleManager.get("login.password"));
        loginBtn.setText(LocaleManager.get("login.btn.login"));
        registerBtn.setText(LocaleManager.get("login.btn.register"));
        exitBtn.setText(LocaleManager.get("login.btn.exit"));
        langLabel.setText(LocaleManager.get("main.language"));
    }

    private static class LocaleListCell extends ListCell<Locale> {
        @Override
        protected void updateItem(Locale item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty || item == null ? null : LocaleManager.getDisplayName(item));
        }
    }
}
