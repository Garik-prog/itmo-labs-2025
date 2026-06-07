package client.gui;

import client.LocaleManager;
import common.Response;
import common.commands.*;
import common.models.Flat;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainController {

    private final Stage stage;

    // Data
    private final ObservableList<Flat> masterList = FXCollections.observableArrayList();
    private FilteredList<Flat> filteredList;

    // UI references for locale refresh
    private Label userLabel;
    private Label langLabel;
    private TextField filterField;
    private Button addBtn, editBtn, deleteBtn, clearBtn, refreshBtn, historyBtn, infoBtn, helpBtn;
    private TableColumn<Flat, Integer> colId;
    private TableColumn<Flat, String> colKey, colName, colX, colY, colArea, colRooms;
    private TableColumn<Flat, String> colFurnish, colView, colTransport, colHouse, colOwner, colDate;
    private TableView<Flat> tableView;
    private Label statusLabel;
    private Tab tableTab, canvasTab;
    private CanvasPane canvasPane;

    private ScheduledExecutorService scheduler;

    public MainController(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        stage.setTitle(LocaleManager.get("main.title"));

        // ── Top bar ──────────────────────────────────────────────────────────
        userLabel = new Label(LocaleManager.get("main.user") + " " + MainApp.getCurrentLogin());
        userLabel.setStyle("-fx-font-weight:bold;-fx-font-size:13;");

        langLabel = new Label(LocaleManager.get("main.language"));
        ComboBox<Locale> langBox = new ComboBox<>();
        langBox.getItems().addAll(LocaleManager.getSupportedLocales());
        langBox.setValue(LocaleManager.getLocale());
        langBox.setCellFactory(lv -> new LocaleCell());
        langBox.setButtonCell(new LocaleCell());
        langBox.setOnAction(e -> {
            if (langBox.getValue() != null) {
                LocaleManager.setLocale(langBox.getValue());
                refreshLocale();
            }
        });

        addBtn      = styledBtn("#27ae60", LocaleManager.get("btn.add"));
        editBtn     = styledBtn("#2980b9", LocaleManager.get("btn.edit"));
        deleteBtn   = styledBtn("#c0392b", LocaleManager.get("btn.delete"));
        clearBtn    = styledBtn("#e67e22", LocaleManager.get("btn.clear"));
        refreshBtn  = styledBtn("#95a5a6", LocaleManager.get("btn.refresh"));
        historyBtn  = styledBtn("#8e44ad", LocaleManager.get("btn.history"));
        infoBtn     = styledBtn("#16a085", LocaleManager.get("btn.info"));
        helpBtn     = styledBtn("#7f8c8d", LocaleManager.get("btn.help"));

        addBtn.setOnAction(e -> handleAdd());
        editBtn.setOnAction(e -> handleEdit());
        deleteBtn.setOnAction(e -> handleDelete());
        clearBtn.setOnAction(e -> handleClear());
        refreshBtn.setOnAction(e -> refreshData());
        historyBtn.setOnAction(e -> handleHistory());
        infoBtn.setOnAction(e -> handleInfo());
        helpBtn.setOnAction(e -> handleHelp());

        HBox topBar = new HBox(8,
                userLabel,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                langLabel, langBox,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                addBtn, editBtn, deleteBtn, clearBtn,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                refreshBtn, historyBtn, infoBtn, helpBtn
        );
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(8, 12, 8, 12));
        topBar.setStyle("-fx-background-color:#2c3e50;");
        for (javafx.scene.Node n : topBar.getChildren()) {
            if (n instanceof Label) ((Label) n).setTextFill(Color.WHITE);
        }

        // ── Filter bar ───────────────────────────────────────────────────────
        Label filterLbl = new Label(LocaleManager.get("filter.label"));
        filterLbl.setStyle("-fx-text-fill:white;");
        filterField = new TextField();
        filterField.setPromptText(LocaleManager.get("filter.prompt"));
        filterField.setPrefWidth(250);
        HBox filterBar = new HBox(8, filterLbl, filterField);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(6, 12, 6, 12));
        filterBar.setStyle("-fx-background-color:#34495e;");

        // ── Table ────────────────────────────────────────────────────────────
        tableView = buildTable();

        filteredList = new FilteredList<>(masterList, p -> true);
        filterField.textProperty().addListener((obs, old, val) ->
                filteredList.setPredicate(flat -> {
                    if (val == null || val.isEmpty()) return true;
                    String lower = val.toLowerCase();
                    return Stream.of(
                                    String.valueOf(flat.getId()),
                                    flat.getMapKey(),
                                    flat.getName(),
                                    flat.getOwnerLogin(),
                                    flat.getFurnish() != null ? flat.getFurnish().name() : "",
                                    flat.getView() != null ? flat.getView().name() : "",
                                    flat.getTransport() != null ? flat.getTransport().name() : "",
                                    String.valueOf(flat.getArea()),
                                    String.valueOf(flat.getNumberOfRooms())
                            ).filter(Objects::nonNull)
                            .anyMatch(s -> s.toLowerCase().contains(lower));
                })
        );

        SortedList<Flat> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

        VBox tableBox = new VBox(filterBar, tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        // ── Canvas ───────────────────────────────────────────────────────────
        canvasPane = new CanvasPane();
        canvasPane.setOnFlatClick(this::showFlatInfo);

        // ── Tab pane ─────────────────────────────────────────────────────────
        tableTab = new Tab(LocaleManager.get("main.tab.table"), tableBox);
        tableTab.setClosable(false);
        canvasTab = new Tab(LocaleManager.get("main.tab.canvas"), canvasPane);
        canvasTab.setClosable(false);
        TabPane tabPane = new TabPane(tableTab, canvasTab);

        // ── Status bar ────────────────────────────────────────────────────────
        statusLabel = new Label(LocaleManager.get("status.ready"));
        statusLabel.setPadding(new Insets(4, 12, 4, 12));
        statusLabel.setStyle("-fx-background-color:#ecf0f1;");
        statusLabel.setMaxWidth(Double.MAX_VALUE);

        // ── Root ─────────────────────────────────────────────────────────────
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(tabPane);
        root.setBottom(statusLabel);

        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(500);
        stage.show();

        startAutoRefresh();
        refreshData();
    }

    // ── Table builder ─────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private TableView<Flat> buildTable() {
        TableView<Flat> tv = new TableView<>();
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        colId        = intCol("col.id",        f -> f.getId());
        colKey       = strCol("col.key",        f -> f.getMapKey() != null ? f.getMapKey() : "");
        colName      = strCol("col.name",       Flat::getName);
        colX         = strCol("col.x",          f -> LocaleManager.getNumberFormat().format(f.getCoordinates().getX()));
        colY         = strCol("col.y",          f -> LocaleManager.getNumberFormat().format(f.getCoordinates().getY()));
        colArea      = strCol("col.area",       f -> LocaleManager.getNumberFormat().format(f.getArea()));
        colRooms     = strCol("col.rooms",      f -> LocaleManager.getNumberFormat().format(f.getNumberOfRooms()));
        colFurnish   = strCol("col.furnish",    f -> f.getFurnish() != null ? f.getFurnish().name() : "—");
        colView      = strCol("col.view",       f -> f.getView() != null ? f.getView().name() : "—");
        colTransport = strCol("col.transport",  f -> f.getTransport() != null ? f.getTransport().name() : "—");
        colHouse     = strCol("col.house",      f -> f.getHouse() != null ? f.getHouse().toString() : "—");
        colOwner     = strCol("col.owner",      Flat::getOwnerLogin);
        colDate      = strCol("col.date",       f -> {
            if (f.getCreationDate() == null) return "—";
            DateFormat fmt = LocaleManager.getDateTimeFormat();
            return fmt.format(f.getCreationDate());
        });

        colId.setPrefWidth(45);
        colKey.setPrefWidth(70);
        colDate.setPrefWidth(130);

        tv.getColumns().addAll(colId, colKey, colName, colX, colY, colArea, colRooms,
                colFurnish, colView, colTransport, colHouse, colOwner, colDate);

        // Double-click → edit
        tv.setRowFactory(t -> {
            TableRow<Flat> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    handleEditFlat(row.getItem());
                }
            });
            return row;
        });

        return tv;
    }

    private <T extends Number> TableColumn<Flat, T> intCol(String key, java.util.function.Function<Flat, T> getter) {
        TableColumn<Flat, T> col = new TableColumn<>(LocaleManager.get(key));
        col.setCellValueFactory(cd -> new SimpleObjectProperty<>(getter.apply(cd.getValue())));
        return col;
    }

    private TableColumn<Flat, String> strCol(String key, java.util.function.Function<Flat, String> getter) {
        TableColumn<Flat, String> col = new TableColumn<>(LocaleManager.get(key));
        col.setCellValueFactory(cd -> new SimpleStringProperty(getter.apply(cd.getValue())));
        return col;
    }

    // ── Data refresh ─────────────────────────────────────────────────────────

    private void startAutoRefresh() {
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "auto-refresh");
            t.setDaemon(true);
            return t;
        });
        scheduler.scheduleAtFixedRate(this::refreshData, 5, 5, TimeUnit.SECONDS);
    }

    private void refreshData() {
        setStatus(LocaleManager.get("status.loading"));
        new Thread(() -> {
            try {
                ShowCommand cmd = new ShowCommand();
                cmd.setCredentials(MainApp.getCurrentLogin(), MainApp.getCurrentPassword());
                Response resp = MainApp.getNetwork().sendCommand(cmd);
                List<Flat> flats = resp.flats() != null ? resp.flats() : Collections.emptyList();
                Platform.runLater(() -> updateData(flats));
            } catch (Exception e) {
                Platform.runLater(() -> setStatus(LocaleManager.get("status.error") + " " + e.getMessage()));
            }
        }, "refresh-task").start();
    }

    private void updateData(List<Flat> flats) {
        masterList.setAll(flats);
        canvasPane.setFlats(flats);
        setStatus(LocaleManager.get("status.updated") + " — "
                + LocaleManager.getDateTimeFormat().format(new Date()));
    }

    // ── Commands ──────────────────────────────────────────────────────────────

    private void handleAdd() {
        FlatEditDialog.showAdd().ifPresent(data -> {
            runCommand(() -> {
                InsertCommand cmd = new InsertCommand();
                cmd.setKey(data.key());
                cmd.setFlat(data.flat());
                cmd.setCredentials(MainApp.getCurrentLogin(), MainApp.getCurrentPassword());
                return MainApp.getNetwork().sendCommand(cmd);
            });
        });
    }

    private void handleEdit() {
        Flat selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) { alert(LocaleManager.get("btn.edit"), "Select a flat first."); return; }
        handleEditFlat(selected);
    }

    private void handleEditFlat(Flat flat) {
        String myLogin = MainApp.getCurrentLogin();
        if (!myLogin.equals(flat.getOwnerLogin())) {
            alert(LocaleManager.get("btn.edit"), "You can only edit your own flats.");
            return;
        }
        FlatEditDialog.showEdit(flat.getMapKey(), flat).ifPresent(data -> {
            runCommand(() -> {
                UpdateCommand cmd = new UpdateCommand();
                cmd.setId(flat.getId());
                cmd.setFlat(data.flat());
                cmd.setCredentials(myLogin, MainApp.getCurrentPassword());
                return MainApp.getNetwork().sendCommand(cmd);
            });
        });
    }

    private void handleDelete() {
        Flat selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) { alert(LocaleManager.get("btn.delete"), "Select a flat first."); return; }

        if (!confirm(LocaleManager.get("confirm.delete"))) return;

        runCommand(() -> {
            RemoveKeyCommand cmd = new RemoveKeyCommand(selected.getMapKey());
            cmd.setCredentials(MainApp.getCurrentLogin(), MainApp.getCurrentPassword());
            return MainApp.getNetwork().sendCommand(cmd);
        });
    }

    private void handleClear() {
        if (!confirm(LocaleManager.get("confirm.clear"))) return;
        runCommand(() -> {
            ClearCommand cmd = new ClearCommand();
            cmd.setCredentials(MainApp.getCurrentLogin(), MainApp.getCurrentPassword());
            return MainApp.getNetwork().sendCommand(cmd);
        });
    }

    private void handleHistory() {
        runCommandWithResult(() -> {
            HistoryCommand cmd = new HistoryCommand();
            cmd.setCredentials(MainApp.getCurrentLogin(), MainApp.getCurrentPassword());
            return MainApp.getNetwork().sendCommand(cmd);
        }, LocaleManager.get("btn.history"));
    }

    private void handleInfo() {
        runCommandWithResult(() -> {
            InfoCommand cmd = new InfoCommand();
            cmd.setCredentials(MainApp.getCurrentLogin(), MainApp.getCurrentPassword());
            return MainApp.getNetwork().sendCommand(cmd);
        }, LocaleManager.get("btn.info"));
    }

    private void handleHelp() {
        StringBuilder sb = new StringBuilder();
        List<String> cmds = List.of(
                "show — показать все объекты",
                "insert <key> — добавить объект",
                "update <id> — обновить объект",
                "remove_key <key> — удалить по ключу",
                "clear — очистить свои объекты",
                "history — история команд",
                "info — информация о коллекции",
                "min_by_creation_date — минимальный по дате",
                "count_by_view <view> — количество по виду",
                "print_ascending — по возрастанию",
                "remove_lower — удалить меньшие",
                "replace_if_lower <key> — заменить если меньше"
        );
        cmds.forEach(c -> sb.append(c).append("\n"));
        alert(LocaleManager.get("btn.help"), sb.toString());
    }

    private void showFlatInfo(Flat flat) {
        DateFormat fmt = LocaleManager.getDateTimeFormat();
        String text = "ID: " + flat.getId() + "\n"
                + LocaleManager.get("col.key") + ": " + flat.getMapKey() + "\n"
                + LocaleManager.get("col.name") + ": " + flat.getName() + "\n"
                + LocaleManager.get("col.x") + ": " + flat.getCoordinates().getX() + "\n"
                + LocaleManager.get("col.y") + ": " + flat.getCoordinates().getY() + "\n"
                + LocaleManager.get("col.area") + ": " + LocaleManager.getNumberFormat().format(flat.getArea()) + "\n"
                + LocaleManager.get("col.rooms") + ": " + flat.getNumberOfRooms() + "\n"
                + LocaleManager.get("col.furnish") + ": " + (flat.getFurnish() != null ? flat.getFurnish() : "—") + "\n"
                + LocaleManager.get("col.view") + ": " + (flat.getView() != null ? flat.getView() : "—") + "\n"
                + LocaleManager.get("col.transport") + ": " + (flat.getTransport() != null ? flat.getTransport() : "—") + "\n"
                + LocaleManager.get("col.house") + ": " + (flat.getHouse() != null ? flat.getHouse() : "—") + "\n"
                + LocaleManager.get("col.owner") + ": " + flat.getOwnerLogin() + "\n"
                + LocaleManager.get("col.date") + ": " + (flat.getCreationDate() != null ? fmt.format(flat.getCreationDate()) : "—");

        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(LocaleManager.get("dialog.info.title"));
        a.setHeaderText(flat.getName() + " (#" + flat.getId() + ")");
        a.setContentText(text);
        // Allow editing from info dialog if the flat belongs to the user
        if (MainApp.getCurrentLogin().equals(flat.getOwnerLogin())) {
            ButtonType editType = new ButtonType(LocaleManager.get("btn.edit"), ButtonBar.ButtonData.LEFT);
            a.getButtonTypes().add(0, editType);
            a.showAndWait().ifPresent(btn -> {
                if (btn == editType) handleEditFlat(flat);
            });
        } else {
            a.show();
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    @FunctionalInterface
    private interface CommandSupplier {
        Response get() throws Exception;
    }

    private void runCommand(CommandSupplier supplier) {
        setStatus(LocaleManager.get("status.loading"));
        new Thread(() -> {
            try {
                Response resp = supplier.get();
                Platform.runLater(() -> {
                    setStatus(resp.message());
                    if (resp.isSuccess()) refreshData();
                });
            } catch (Exception e) {
                Platform.runLater(() -> setStatus(LocaleManager.get("status.error") + " " + e.getMessage()));
            }
        }, "cmd-thread").start();
    }

    private void runCommandWithResult(CommandSupplier supplier, String title) {
        new Thread(() -> {
            try {
                Response resp = supplier.get();
                Platform.runLater(() -> alert(title, resp.message()));
            } catch (Exception e) {
                Platform.runLater(() -> alert(title, e.getMessage()));
            }
        }, "cmd-thread").start();
    }

    private boolean confirm(String message) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(LocaleManager.get("confirm.title"));
        a.setContentText(message);
        ButtonType yes = new ButtonType(LocaleManager.get("confirm.yes"), ButtonBar.ButtonData.YES);
        ButtonType no  = new ButtonType(LocaleManager.get("confirm.no"),  ButtonBar.ButtonData.NO);
        a.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> r = a.showAndWait();
        return r.isPresent() && r.get() == yes;
    }

    private void alert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.show();
    }

    private void setStatus(String text) {
        Platform.runLater(() -> statusLabel.setText(text));
    }

    private Button styledBtn(String color, String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color:" + color + ";-fx-text-fill:white;-fx-background-radius:4;");
        return btn;
    }

    // ── Locale refresh ────────────────────────────────────────────────────────

    private void refreshLocale() {
        stage.setTitle(LocaleManager.get("main.title"));
        userLabel.setText(LocaleManager.get("main.user") + " " + MainApp.getCurrentLogin());
        langLabel.setText(LocaleManager.get("main.language"));
        filterField.setPromptText(LocaleManager.get("filter.prompt"));

        addBtn.setText(LocaleManager.get("btn.add"));
        editBtn.setText(LocaleManager.get("btn.edit"));
        deleteBtn.setText(LocaleManager.get("btn.delete"));
        clearBtn.setText(LocaleManager.get("btn.clear"));
        refreshBtn.setText(LocaleManager.get("btn.refresh"));
        historyBtn.setText(LocaleManager.get("btn.history"));
        infoBtn.setText(LocaleManager.get("btn.info"));
        helpBtn.setText(LocaleManager.get("btn.help"));

        tableTab.setText(LocaleManager.get("main.tab.table"));
        canvasTab.setText(LocaleManager.get("main.tab.canvas"));

        colId.setText(LocaleManager.get("col.id"));
        colKey.setText(LocaleManager.get("col.key"));
        colName.setText(LocaleManager.get("col.name"));
        colX.setText(LocaleManager.get("col.x"));
        colY.setText(LocaleManager.get("col.y"));
        colArea.setText(LocaleManager.get("col.area"));
        colRooms.setText(LocaleManager.get("col.rooms"));
        colFurnish.setText(LocaleManager.get("col.furnish"));
        colView.setText(LocaleManager.get("col.view"));
        colTransport.setText(LocaleManager.get("col.transport"));
        colHouse.setText(LocaleManager.get("col.house"));
        colOwner.setText(LocaleManager.get("col.owner"));
        colDate.setText(LocaleManager.get("col.date"));

        tableView.refresh();
        statusLabel.setText(LocaleManager.get("status.ready"));
    }

    private static class LocaleCell extends ListCell<Locale> {
        @Override
        protected void updateItem(Locale item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty || item == null ? null : LocaleManager.getDisplayName(item));
        }
    }
}
