package client.gui;

import client.LocaleManager;
import common.models.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class FlatEditDialog {

    public static Optional<FlatFormData> showAdd() {
        return showDialog(null, null);
    }

    public static Optional<FlatFormData> showEdit(String currentKey, Flat flat) {
        return showDialog(currentKey, flat);
    }

    private static Optional<FlatFormData> showDialog(String existingKey, Flat existingFlat) {
        boolean isAdd = (existingFlat == null);

        Dialog<FlatFormData> dialog = new Dialog<>();
        dialog.setTitle(isAdd
                ? LocaleManager.get("dialog.add.title")
                : LocaleManager.get("dialog.edit.title"));

        ButtonType okType = new ButtonType(LocaleManager.get("dialog.btn.ok"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelType = new ButtonType(LocaleManager.get("dialog.btn.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okType, cancelType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        int row = 0;

        // Key (add only)
        TextField keyField = new TextField();
        keyField.setPromptText("myKey1");
        if (isAdd) {
            grid.addRow(row++, new Label(LocaleManager.get("dialog.key")), keyField);
        }

        // Name
        TextField nameField = new TextField(existingFlat != null ? existingFlat.getName() : "");
        nameField.setPromptText("Flat name");
        grid.addRow(row++, new Label(LocaleManager.get("dialog.name")), nameField);

        // Coordinates
        TextField xField = new TextField(existingFlat != null
                ? String.valueOf(existingFlat.getCoordinates().getX()) : "");
        xField.setPromptText("0");
        grid.addRow(row++, new Label(LocaleManager.get("dialog.coord.x")), xField);

        TextField yField = new TextField(existingFlat != null
                ? String.valueOf(existingFlat.getCoordinates().getY()) : "");
        yField.setPromptText("0");
        grid.addRow(row++, new Label(LocaleManager.get("dialog.coord.y")), yField);

        // Area
        TextField areaField = new TextField(existingFlat != null
                ? String.valueOf(existingFlat.getArea()) : "");
        areaField.setPromptText("50");
        grid.addRow(row++, new Label(LocaleManager.get("dialog.area")), areaField);

        // Rooms
        TextField roomsField = new TextField(existingFlat != null
                ? String.valueOf(existingFlat.getNumberOfRooms()) : "");
        roomsField.setPromptText("3");
        grid.addRow(row++, new Label(LocaleManager.get("dialog.rooms")), roomsField);

        // Furnish
        ComboBox<Furnish> furnishBox = new ComboBox<>();
        furnishBox.getItems().add(null);
        furnishBox.getItems().addAll(Furnish.values());
        furnishBox.setValue(existingFlat != null ? existingFlat.getFurnish() : null);
        grid.addRow(row++, new Label(LocaleManager.get("dialog.furnish")), furnishBox);

        // View
        ComboBox<View> viewBox = new ComboBox<>();
        viewBox.getItems().add(null);
        viewBox.getItems().addAll(View.values());
        viewBox.setValue(existingFlat != null ? existingFlat.getView() : null);
        grid.addRow(row++, new Label(LocaleManager.get("dialog.view")), viewBox);

        // Transport
        ComboBox<Transport> transportBox = new ComboBox<>();
        transportBox.getItems().add(null);
        transportBox.getItems().addAll(Transport.values());
        transportBox.setValue(existingFlat != null ? existingFlat.getTransport() : null);
        grid.addRow(row++, new Label(LocaleManager.get("dialog.transport")), transportBox);

        // House separator
        grid.addRow(row++, new Separator(), new Separator());

        // House name
        TextField houseNameField = new TextField();
        if (existingFlat != null && existingFlat.getHouse() != null) {
            houseNameField.setText(existingFlat.getHouse().getName());
        }
        houseNameField.setPromptText(LocaleManager.get("dialog.house.name"));
        grid.addRow(row++, new Label(LocaleManager.get("dialog.house.name")), houseNameField);

        // House year
        TextField houseYearField = new TextField();
        if (existingFlat != null && existingFlat.getHouse() != null) {
            houseYearField.setText(String.valueOf(existingFlat.getHouse().getYear()));
        }
        houseYearField.setPromptText("2000");
        grid.addRow(row++, new Label(LocaleManager.get("dialog.house.year")), houseYearField);

        // House flats on floor
        TextField houseFloorsField = new TextField();
        if (existingFlat != null && existingFlat.getHouse() != null
                && existingFlat.getHouse().getNumberOfFlatsOnFloor() != null) {
            houseFloorsField.setText(String.valueOf(existingFlat.getHouse().getNumberOfFlatsOnFloor()));
        }
        houseFloorsField.setPromptText("8");
        grid.addRow(row, new Label(LocaleManager.get("dialog.house.floors")), houseFloorsField);

        dialog.getDialogPane().setContent(grid);

        // Validation
        Node okButton = dialog.getDialogPane().lookupButton(okType);
        okButton.setDisable(false);

        dialog.setResultConverter(btn -> {
            if (btn != okType) return null;
            try {
                String key = isAdd ? keyField.getText().trim() : existingKey;
                if (isAdd && key.isEmpty()) {
                    showError(LocaleManager.get("dialog.key") + " is required");
                    return null;
                }
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    showError(LocaleManager.get("dialog.name") + " is required");
                    return null;
                }
                int x = Integer.parseInt(xField.getText().trim());
                long y = Long.parseLong(yField.getText().trim());
                long area = Long.parseLong(areaField.getText().trim());
                if (area <= 0) { showError("Area must be > 0"); return null; }
                int rooms = Integer.parseInt(roomsField.getText().trim());
                if (rooms <= 0) { showError("Rooms must be > 0"); return null; }

                Flat flat = new Flat();
                flat.setName(name);
                Coordinates coords = new Coordinates();
                coords.setX(x);
                coords.setY(y);
                flat.setCoordinates(coords);
                flat.setArea(area);
                flat.setNumberOfRooms(rooms);
                flat.setFurnish(furnishBox.getValue());
                flat.setView(viewBox.getValue());
                flat.setTransport(transportBox.getValue());

                String hn = houseNameField.getText().trim();
                String hyStr = houseYearField.getText().trim();
                String hfStr = houseFloorsField.getText().trim();
                if (!hn.isEmpty() && !hyStr.isEmpty()) {
                    House house = new House();
                    house.setName(hn);
                    house.setYear(Integer.parseInt(hyStr));
                    if (!hfStr.isEmpty()) house.setNumberOfFlatsOnFloor(Integer.parseInt(hfStr));
                    flat.setHouse(house);
                }

                return new FlatFormData(key, flat);

            } catch (NumberFormatException e) {
                showError("Invalid number: " + e.getMessage());
                return null;
            }
        });

        return dialog.showAndWait();
    }

    private static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public record FlatFormData(String key, Flat flat) {}
}
