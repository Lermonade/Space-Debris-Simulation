package com.lerstudios.space_debris_simulation.modules.configuration.uiBuilders;

import com.lerstudios.space_debris_simulation.modules.configuration.SimulationSettings;
import com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.removalMethods.OrbitalLaserRemovalMethod;
import com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.removalMethods.RemovalMethodTemplate;
import com.lerstudios.space_debris_simulation.types.RemovalMethodType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

        // --- Laser fields ---
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

        // --- Orbital fields ---
        TextField semiMajorAxisField = new TextField(source.semiMajorAxisMeters);
        TextField eccentricityField = new TextField(source.eccentricity);
        TextField inclinationField = new TextField(source.inclinationDegrees);
        TextField raanField = new TextField(source.rightAscensionOfAscendingNodeDegrees);
        TextField argPeriapsisField = new TextField(source.argumentOfPeriapsisDegrees);
        TextField trueAnomalyField = new TextField(source.trueAnomalyDegrees);

        TextField massField = new TextField(source.massKg);
        TextField collisionRadiusField = new TextField(source.collisionRadiusMeters);

        // --- Color / draw line fields ---
        TextField colorField = new TextField(source.color);
        TextField laserColorField = new TextField(source.laserColor);
        TextField drawLinesField = new TextField(source.drawLines);

        // --- Propagation method ComboBox ---
        ComboBox<String> propagationMethodBox = new ComboBox<>();
        propagationMethodBox.getItems().addAll(
                "Two-Body Keplerian",
                "Simplified Velocity Verlet N-Body",
                "Dynamic Propagation Switching"
        );
        propagationMethodBox.setValue(source.propagationMethod);

        if (!sourceList.contains(source)) {
            sourceList.add(source);
        }

        // --- Listeners ---
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

        semiMajorAxisField.textProperty().addListener((o, a, n) -> {
            source.semiMajorAxisMeters = n;
            source.rebuildOrbitalData();
        });

        eccentricityField.textProperty().addListener((o, a, n) -> {
            source.eccentricity = n;
            source.rebuildOrbitalData();
        });

        inclinationField.textProperty().addListener((o, a, n) -> {
            source.inclinationDegrees = n;
            source.rebuildOrbitalData();
        });

        raanField.textProperty().addListener((o, a, n) -> {
            source.rightAscensionOfAscendingNodeDegrees = n;
            source.rebuildOrbitalData();
        });

        argPeriapsisField.textProperty().addListener((o, a, n) -> {
            source.argumentOfPeriapsisDegrees = n;
            source.rebuildOrbitalData();
        });

        trueAnomalyField.textProperty().addListener((o, a, n) -> {
            source.trueAnomalyDegrees = n;
            source.rebuildOrbitalData();
        });

        massField.textProperty().addListener((o,a,n) -> source.massKg = n);
        collisionRadiusField.textProperty().addListener((o,a,n) -> source.collisionRadiusMeters = n);

        colorField.textProperty().addListener((o,a,n) -> source.color = n);
        laserColorField.textProperty().addListener((o,a,n) -> source.laserColor = n);
        drawLinesField.textProperty().addListener((o,a,n) -> source.drawLines = n);

        propagationMethodBox.valueProperty().addListener((o,a,n) -> source.propagationMethod = n);

        // --- Delete button ---
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

        // --- Add all rows ---
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

                // Orbital rows
                createRow.apply("Semi-Major Axis (m)", semiMajorAxisField),
                createRow.apply("Eccentricity", eccentricityField),
                createRow.apply("Inclination (°)", inclinationField),
                createRow.apply("RAAN (°)", raanField),
                createRow.apply("Argument of Periapsis (°)", argPeriapsisField),
                createRow.apply("True Anomaly (°)", trueAnomalyField),
                createRow.apply("Mass (kg)", massField),
                createRow.apply("Collision Radius (m)", collisionRadiusField),

                // Color / draw line
                createRow.apply("Color", colorField),
                createRow.apply("Laser Color", laserColorField),
                createRow.apply("Draw Lines", drawLinesField),

                // Propagation method
                createRow.apply("Propagation Method", propagationMethodBox),

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
                        "LADROIT",

                        // ----------------------
                        // Laser parameters
                        // ----------------------
                        "21000",   // Laser Power (W) – 21 kW burst
                        "355",     // Wavelength (nm)
                        "1.5",     // Aperture Diameter (m)
                        "2.0",     // Beam Quality (M²)
                        "50",      // Optical Efficiency (%)

                        "32",      // Pulse Rate (Hz)
                        "0.1",     // Pulse Duration (ns -> convert if needed to ps, 0.1 ns = 100 ps)

                        "10",      // Max Engagement Time (s)
                        "120",     // Cooldown Time (s)
                        "30",      // Slew Rate (deg/s)
                        "775000",  // Targeting Range (m) – average from 250–775 km

                        // ----------------------
                        // Orbital elements + mass
                        // ----------------------
                        "8180000",  // Semi-major axis (m) – average of 560–960 km altitude + Earth's radius
                        "0.028",    // Eccentricity
                        "90",       // Inclination (deg)
                        "0",        // RAAN (deg) – unspecified, can leave as 0
                        "-180",     // Argument of periapsis (deg)
                        "0",        // True anomaly (deg) – initial position
                        "8000",     // Mass (kg) – L’ADROIT satellite

                        // ----------------------
                        // Collision radius
                        // ----------------------
                        "5",        // Collision radius (m) – platform safety buffer

                        // ----------------------
                        // Display properties
                        // ----------------------
                        "#0000FF",   // color: blue
                        "#FF00FF",   // laserColor: purple/pink
                        "true",      // drawLines

                        // ----------------------
                        // Propagation
                        // ----------------------
                        "Two-Body Keplerian" // propagationMethod
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
