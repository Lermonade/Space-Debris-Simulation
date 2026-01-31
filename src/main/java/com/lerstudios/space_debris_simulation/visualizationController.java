package com.lerstudios.space_debris_simulation;

import com.lerstudios.space_debris_simulation.visualizationUtilities.KeplerianVisualizationObject;
import com.lerstudios.space_debris_simulation.visualizationUtilities.Timing;
import com.lerstudios.space_debris_simulation.visualizationUtilities.VisualizationGraphics;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class visualizationController {

    @FXML
    private AnchorPane rootPane; // 3D Root Pane

    @FXML
    private Label timeDisplay; // Top time display text

    @FXML
    private Label timeTextDisplay; // Bottom time display text

    @FXML
    private Button pausePlayButton;

    @FXML
    private Button speedButton;

    @FXML
    private Button slowButton;

    @FXML
    private Button startButton;

    @FXML
    private Button endButton;

    Timing timing;
    VisualizationGraphics visualization;

    KeplerianVisualizationObject object1;

    Map<String, KeplerianVisualizationObject> keplerianObjects;

    public static void generateRandomLEODebris(
            Map<String, KeplerianVisualizationObject> keplerianObjects,
            int count) {
        Random rand = new Random();

        final double EARTH_RADIUS = 6371000; // meters

        for (int i = 0; i < count; i++) {

            // Altitude distribution (meters)
            double altitude;
            double altChoice = rand.nextDouble();

            if (altChoice < 0.35) {
                // ISS-like band
                altitude = 400_000 + rand.nextGaussian() * 40_000;
            } else if (altChoice < 0.75) {
                // Sun-synchronous dominant band
                altitude = 800_000 + rand.nextGaussian() * 120_000;
            } else {
                // Higher LEO
                altitude = 1_200_000 + rand.nextGaussian() * 300_000;
            }

            altitude = Math.max(300_000, Math.min(altitude, 2_000_000));
            double semiMajorAxis = EARTH_RADIUS + altitude;

            // Eccentricity (near circular)
            double eccentricity = Math.abs(rand.nextGaussian()) * 0.01;
            eccentricity = Math.min(eccentricity, 0.05);

            // Inclination distribution
            double inclination;
            double incChoice = rand.nextDouble();

            if (incChoice < 0.30) {
                inclination = Math.toRadians(51.6 + rand.nextGaussian() * 1.5);
            } else if (incChoice < 0.65) {
                inclination = Math.toRadians(98.0 + rand.nextGaussian() * 1.0);
            } else if (incChoice < 0.85) {
                inclination = Math.toRadians(82.0 + rand.nextGaussian() * 2.0);
            } else {
                inclination = Math.toRadians(90.0 + rand.nextGaussian() * 5.0);
            }

            inclination = Math.max(0, Math.min(inclination, Math.PI));

            // Angular elements (uniform)
            double raan = rand.nextDouble() * 2 * Math.PI;
            double argPeriapsis = rand.nextDouble() * 2 * Math.PI;
            double trueAnomaly = rand.nextDouble() * 2 * Math.PI;

            keplerianObjects.put(
                    "orbit" + i,
                    new KeplerianVisualizationObject(
                            semiMajorAxis,
                            eccentricity,
                            inclination,
                            raan,
                            argPeriapsis,
                            trueAnomaly
                    )
            );
        }
    }

    @FXML
    public void initialize() {

        keplerianObjects = new HashMap<>();
        /*keplerianObjects.put("orbit1", new KeplerianVisualizationObject(6771000, 0.03, 0, Math.toRadians(51.6), 0, 0));
        keplerianObjects.put("orbit2", new KeplerianVisualizationObject(7078000, 0.001, Math.toRadians(45), Math.toRadians(98.2), Math.toRadians(30), Math.toRadians(10)));
        keplerianObjects.put("orbit3", new KeplerianVisualizationObject(6900000, 0.01, Math.toRadians(90), Math.toRadians(90), Math.toRadians(120), Math.toRadians(200)));
        keplerianObjects.put("orbit4", new KeplerianVisualizationObject(6800000, 0.02, Math.toRadians(15), Math.toRadians(52), Math.toRadians(210), Math.toRadians(45)));
        keplerianObjects.put("orbit5", new KeplerianVisualizationObject(6600000, 0.05, Math.toRadians(270), Math.toRadians(28.5), Math.toRadians(75), Math.toRadians(300)));
        keplerianObjects.put("orbit6", new KeplerianVisualizationObject(7200000, 0.015, Math.toRadians(60), Math.toRadians(120), Math.toRadians(10), Math.toRadians(90)));
        keplerianObjects.put("orbit7", new KeplerianVisualizationObject(7500000, 0.08, Math.toRadians(110), Math.toRadians(63.4), Math.toRadians(300), Math.toRadians(180)));
        keplerianObjects.put("orbit8", new KeplerianVisualizationObject(6950000, 0.025, Math.toRadians(200), Math.toRadians(40), Math.toRadians(155), Math.toRadians(20)));
        keplerianObjects.put("orbit9", new KeplerianVisualizationObject(6780000, 0.0005, 0, Math.toRadians(0), Math.toRadians(45), Math.toRadians(270)));
        keplerianObjects.put("orbit10", new KeplerianVisualizationObject(7100000, 0.02, Math.toRadians(330), Math.toRadians(75), Math.toRadians(200), Math.toRadians(135)));*/

        //generateRandomLEODebris(keplerianObjects, 60000);


        visualization = new VisualizationGraphics(rootPane);
        timing = new Timing(this::updateScene, timeDisplay, timeTextDisplay, pausePlayButton, speedButton, slowButton, startButton, endButton);

        for (Map.Entry<String, KeplerianVisualizationObject> entry : keplerianObjects.entrySet()) {
            KeplerianVisualizationObject orbit = entry.getValue();

            //orbit.createOrbitalPath();
            visualization.add3DObjectToGroup(orbit.get3DObject());
            //visualization.addGrouptoGroup(orbit.getPathObject());
        }
    }

    public void updateScene(double seconds) {
        for (Map.Entry<String, KeplerianVisualizationObject> entry : keplerianObjects.entrySet()) {
            KeplerianVisualizationObject orbit = entry.getValue();

            orbit.moveToOrbitalPosition(seconds);
        }
    }

    public void switchToScene1(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(getClass().getResource("main.fxml"))
        );
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}