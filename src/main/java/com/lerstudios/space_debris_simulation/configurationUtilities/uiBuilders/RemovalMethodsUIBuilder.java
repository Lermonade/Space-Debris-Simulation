package com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders;

import com.lerstudios.space_debris_simulation.configurationUtilities.SimulationSettings;
import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.removalMethods.OrbitalLaserRemovalMethod;
import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.removalMethods.RemovalMethodTemplate;
import com.lerstudios.space_debris_simulation.simulation.types.RemovalMethodType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class RemovalMethodsUIBuilder {
    public static void addOrbitalLaserComponent(
            OrbitalLaserRemovalMethod source,
            VBox removalMethodsBox,
            ArrayList<RemovalMethodTemplate> sourceList
    ) {

        VBox sourceVBox = new VBox();
        sourceVBox.setPrefWidth(300);
        sourceVBox.setStyle("-fx-border-color: #000000;");
        sourceVBox.setSpacing(0);

        java.util.function.BiFunction<String, Node, HBox> createRow =
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

        TextField nameField = new TextField(source.name);

        TextField powerField = new TextField(source.laserPowerWatts);
        TextField wavelengthField = new TextField(source.wavelengthNanometers);
        TextField apertureField = new TextField(source.apertureDiameterMeters);
        TextField beamQualityField = new TextField(source.beamQualityM2);
        TextField efficiencyField = new TextField(source.opticalEfficiencyPercent);

        TextField pulseRateField = new TextField(source.pulseRateHz);
        TextField pulseDurationField = new TextField(source.pulseDurationPicoseconds);

        TextField maxEngageField = new TextField(source.maxEngagementTimeSeconds);
        TextField cooldownField = new TextField(source.cooldownTimeSeconds);
        TextField slewRateField = new TextField(source.slewRateDegPerSec);
        TextField rangeField = new TextField(source.targetingRangeMeters);

        if (!sourceList.contains(source)) {
            sourceList.add(source);
        }

        nameField.textProperty().addListener((o,a,n) -> source.name = n);

        powerField.textProperty().addListener((o,a,n) -> source.laserPowerWatts = n);
        wavelengthField.textProperty().addListener((o,a,n) -> source.wavelengthNanometers = n);
        apertureField.textProperty().addListener((o,a,n) -> source.apertureDiameterMeters = n);
        beamQualityField.textProperty().addListener((o,a,n) -> source.beamQualityM2 = n);
        efficiencyField.textProperty().addListener((o,a,n) -> source.opticalEfficiencyPercent = n);

        pulseRateField.textProperty().addListener((o,a,n) -> source.pulseRateHz = n);
        pulseDurationField.textProperty().addListener((o,a,n) -> source.pulseDurationPicoseconds = n);

        maxEngageField.textProperty().addListener((o,a,n) -> source.maxEngagementTimeSeconds = n);
        cooldownField.textProperty().addListener((o,a,n) -> source.cooldownTimeSeconds = n);
        slewRateField.textProperty().addListener((o,a,n) -> source.slewRateDegPerSec = n);
        rangeField.textProperty().addListener((o,a,n) -> source.targetingRangeMeters = n);

        Button deleteButton = new Button("Delete Removal Method");
        deleteButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.setStyle(
                "-fx-background-color: #ef4444;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;"
        );

        deleteButton.setOnAction(e ->
                deleteOrbitalLaserComponent (
                        removalMethodsBox,
                        sourceVBox,
                        sourceList,
                        source
                )
        );

        HBox deleteRow = new HBox(deleteButton);
        deleteRow.setPadding(new Insets(8, 16, 8, 16));

        sourceVBox.getChildren().addAll(
                createRow.apply("Name", nameField),

                createRow.apply("Laser Power (W)", powerField),
                createRow.apply("Wavelength (nm)", wavelengthField),
                createRow.apply("Aperture Diameter (m)", apertureField),
                createRow.apply("Beam Quality (M²)", beamQualityField),
                createRow.apply("Optical Efficiency (%)", efficiencyField),

                createRow.apply("Pulse Rate (Hz)", pulseRateField),
                createRow.apply("Pulse Duration (ps)", pulseDurationField),

                createRow.apply("Max Engagement Time (s)", maxEngageField),
                createRow.apply("Cooldown Time (s)", cooldownField),
                createRow.apply("Slew Rate (deg/s)", slewRateField),
                createRow.apply("Targeting Range (m)", rangeField),

                deleteRow
        );

        removalMethodsBox.getChildren().add(sourceVBox);
    }

    public static void addOrbitalLaserComponent(
            String name,
            VBox removalMethodsBox,
            ArrayList<RemovalMethodTemplate> sourceList
    ) {

        OrbitalLaserRemovalMethod source =
                new OrbitalLaserRemovalMethod(
                        name,

                        "5000",   // Laser Power (W)
                        "1064",   // Wavelength (nm)
                        "0.4",    // Aperture Diameter (m)
                        "1.5",    // Beam Quality (M²)
                        "35",     // Optical Efficiency (%)

                        "10",     // Pulse Rate (Hz)
                        "50",     // Pulse Duration (ps)

                        "30",     // Max Engagement Time (s)
                        "120",    // Cooldown Time (s)
                        "2",      // Slew Rate (deg/s)
                        "80000"   // Targeting Range (m)
                );

        addOrbitalLaserComponent(source, removalMethodsBox, sourceList);
    }

    public static void deleteOrbitalLaserComponent(
            VBox removalMethodsBox,
            VBox sourceVBox,
            ArrayList<RemovalMethodTemplate> sourceList,
            OrbitalLaserRemovalMethod source
    ) {
        removalMethodsBox.getChildren().remove(sourceVBox);

        sourceList.remove(source);
    }

    public static void loadRemovalMethodsFromSettings(
            SimulationSettings settings,
            VBox removalMethodsBox
    ) {
        removalMethodsBox.getChildren().clear();

        for (RemovalMethodTemplate method : settings.removalmethods) {
            if(method.getRemovalMethodType() == RemovalMethodType.ORBITAL_LASER) {
                addOrbitalLaserComponent(
                        (OrbitalLaserRemovalMethod) method,
                        removalMethodsBox,
                        settings.removalmethods
                );
            }
        }
    }

}
