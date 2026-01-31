package com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders;

import com.lerstudios.space_debris_simulation.configurationUtilities.SimulationSettings;
import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.PopulationObject;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class PopulationsUIBuilder {
    public static void addPopulationComponent(
            PopulationObject pop,
            VBox populationsBox,
            ArrayList<PopulationObject> populationList
    ) {

        VBox populationVBox = new VBox();
        populationVBox.setPrefWidth(300);
        populationVBox.setStyle("-fx-border-color: #000000;");
        populationVBox.setSpacing(0);

        java.util.function.BiFunction<String, javafx.scene.Node, HBox> createRow =
                (labelText, inputNode) -> {

                    Label label = new Label(labelText);
                    label.setFont(Font.font("Consolas", 12));

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    HBox row = new HBox(label, spacer, inputNode);
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setMinHeight(32);
                    row.setPrefHeight(32);
                    row.setMaxHeight(32);
                    row.setPadding(new Insets(0, 16, 0, 16));
                    row.setStyle(
                            "-fx-border-color: transparent transparent #e5e7eb transparent;" +
                                    "-fx-border-width: 0 0 2 0;"
                    );

                    return row;
                };

        TextField populationNameField = new TextField(pop.populationName);

        ComboBox<String> objectClassificationBox = new ComboBox<>();
        objectClassificationBox.getItems().addAll(
                "Debris",
                "Operational Satellite"
        );
        objectClassificationBox.setValue(pop.objectClassification);

        ComboBox<String> propagationMethodBox = new ComboBox<>();
        propagationMethodBox.getItems().addAll(
                "Two-Body Keplerian",
                "Dynamic Propegation Switching"
        );
        propagationMethodBox.setValue(pop.propagationMethod);

        ComboBox<String> renderingMethodBox = new ComboBox<>();
        renderingMethodBox.getItems().addAll(
                "3D Objects",
                "Particle Billboards"
        );
        renderingMethodBox.setValue(pop.renderingMethod);

        TextField colorField = new TextField(
                pop.renderingColor != null ? pop.renderingColor : "#ffffff"
        );

        TextField sourceField = new TextField(
                pop.source != null ? pop.source : "none"
        );
        sourceField.setPromptText("none");

        if (!populationList.contains(pop)) {
            populationList.add(pop);
        }

        populationNameField.textProperty().addListener(
                (obs, o, n) -> pop.populationName = n
        );
        objectClassificationBox.valueProperty().addListener(
                (obs, o, n) -> pop.objectClassification = n
        );
        propagationMethodBox.valueProperty().addListener(
                (obs, o, n) -> pop.propagationMethod = n
        );
        renderingMethodBox.valueProperty().addListener(
                (obs, o, n) -> pop.renderingMethod = n
        );
        colorField.textProperty().addListener(
                (obs, o, n) -> pop.renderingColor = n
        );
        sourceField.textProperty().addListener(
                (obs, o, n) -> pop.source = n
        );

        Button deleteButton = new Button("Delete");
        deleteButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.setStyle(
                "-fx-background-color: #ef4444;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;"
        );

        deleteButton.setOnAction(e ->
                deletePopulationComponent(populationsBox, populationVBox, populationList, pop)
        );

        HBox deleteRow = new HBox(deleteButton);
        deleteRow.setPadding(new Insets(8, 16, 8, 16));

        populationVBox.getChildren().addAll(
                createRow.apply("Population Name", populationNameField),
                createRow.apply("Object Classification", objectClassificationBox),
                createRow.apply("Propagation Method", propagationMethodBox),
                createRow.apply("Rendering Method", renderingMethodBox),
                createRow.apply("Rendering Color", colorField),
                createRow.apply("Source", sourceField),
                deleteRow
        );

        populationsBox.getChildren().add(populationVBox);
    }


    public static void addPopulationComponent(
            String populationName,
            VBox populationsBox,
            ArrayList<PopulationObject> populationList
    ) {
        PopulationObject pop = new PopulationObject(
                populationName,
                "Debris",
                "Two-Body Keplerian",
                "3D Objects",
                "#FFFFFF",
                "none"
        );

        addPopulationComponent(pop, populationsBox, populationList);
    }

    public static void loadPopulationsFromSettings(
            SimulationSettings settings,
            VBox populationsBox
    ) {
        populationsBox.getChildren().clear();

        for (PopulationObject pop : settings.populationObjects) {
            addPopulationComponent(
                    pop,
                    populationsBox,
                    settings.populationObjects
            );
        }
    }

    public static void deletePopulationComponent(
            VBox populationsBox,
            VBox populationVBox,
            ArrayList<PopulationObject> populationList,
            PopulationObject pop
    ) {
        populationsBox.getChildren().remove(populationVBox);
        populationList.remove(pop);
    }

}
