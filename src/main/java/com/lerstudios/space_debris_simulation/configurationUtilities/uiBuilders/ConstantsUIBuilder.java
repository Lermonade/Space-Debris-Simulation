package com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders;

import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.SimulationConstant;
import com.lerstudios.space_debris_simulation.configurationUtilities.SimulationSettings;
import com.lerstudios.space_debris_simulation.utils.Vector3;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ConstantsUIBuilder {

    public static void buildConstantsUI(VBox constantsBox, SimulationSettings settings) {
        constantsBox.getChildren().clear();

        for (SimulationConstant constant : SimulationSettings.templateConstants) {
            if (constant.isNumber()) {
                constantsBox.getChildren().add(createNumberRow(constant, settings));
            } else if (constant.isVector()) {
                constantsBox.getChildren().add(createVectorRow(constant, settings));
            } else if (constant.isString()) {
                constantsBox.getChildren().add(createStringRow(constant, settings));
            }
        }
    }

    private static HBox createNumberRow(SimulationConstant constant, SimulationSettings settings) {
        HBox row = new HBox();
        row.setSpacing(8);

        row.setPadding(new Insets(4, 16, 4, 16));

        Label label = new Label(constant.getName());
        label.setPrefWidth(150);

        TextField textField = new TextField();
        textField.setText(settings.getOrDefault(constant.getName(), constant.getValue()).toString());

        HBox.setHgrow(textField, Priority.ALWAYS);

        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                double value = Double.parseDouble(newVal);
                settings.set(constant.getName(), value);
            } catch (NumberFormatException ignored) {
            }
        });

        row.getChildren().addAll(label, textField);
        return row;
    }

    private static HBox createVectorRow(SimulationConstant constant, SimulationSettings settings) {
        HBox row = new HBox();
        row.setSpacing(10);
        row.setPadding(new Insets(4, 16, 4, 16));
        row.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label(constant.getName());
        label.setMinWidth(150);

        Vector3 vec = (Vector3) settings.getOrDefault(constant.getName(), constant.getValue());

        TextField xField = createVectorComponentField(constant.getName(), "x", vec.x, settings);
        TextField yField = createVectorComponentField(constant.getName(), "y", vec.y, settings);
        TextField zField = createVectorComponentField(constant.getName(), "z", vec.z, settings);

        row.getChildren().addAll(label, xField, yField, zField);
        return row;
    }


    private static TextField createVectorComponentField(String name, String component, double defaultValue, SimulationSettings settings) {
        TextField field = new TextField(String.valueOf(defaultValue));
        field.setPrefWidth(50);
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                double value = Double.parseDouble(newVal);
                Vector3 vec = (Vector3) settings.getOrDefault(name, new Vector3(0, 0, 0));
                switch (component) {
                    case "x" -> vec.x = value;
                    case "y" -> vec.y = value;
                    case "z" -> vec.z = value;
                }
                settings.set(name, vec);
            } catch (NumberFormatException ignored) {
            }
        });
        return field;
    }

    private static HBox createStringRow(SimulationConstant constant, SimulationSettings settings) {
        HBox row = new HBox();
        row.setSpacing(8);
        row.setPadding(new Insets(4, 16, 4, 16));

        Label label = new Label(constant.getName());
        label.setPrefWidth(150);

        TextField textField = new TextField();
        textField.setText(settings.getOrDefault(constant.getName(), constant.getValue()).toString());
        HBox.setHgrow(textField, Priority.ALWAYS);

        textField.textProperty().addListener((obs, oldVal, newVal) ->
                settings.set(constant.getName(), newVal)
        );

        row.getChildren().addAll(label, textField);
        return row;
    }
}
