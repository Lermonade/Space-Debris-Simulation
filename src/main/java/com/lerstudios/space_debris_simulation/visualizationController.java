package com.lerstudios.space_debris_simulation;

import com.lerstudios.space_debris_simulation.simulation.exportFormats.VisualizationFile;
import com.lerstudios.space_debris_simulation.simulation.exportFormats.VisualizationPopulation;
import com.lerstudios.space_debris_simulation.simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.visualizationUtilities.KeplerianVisualizationObject;
import com.lerstudios.space_debris_simulation.visualizationUtilities.Timing;
import com.lerstudios.space_debris_simulation.visualizationUtilities.VisualizationGraphics;
import com.lerstudios.space_debris_simulation.visualizationUtilities.VisualizationMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.*;

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
    VisualizationMain controller;

    public void initVisualization(VisualizationFile file) {

        visualization = new VisualizationGraphics(rootPane);
        controller = new VisualizationMain(file, visualization);
        timing = new Timing(controller::updateScene, timeDisplay, timeTextDisplay, pausePlayButton, speedButton, slowButton, startButton, endButton);

        controller.initializeVisualization();

    }

    @FXML
    public void initialize() {}

    public void switchToScene1(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(getClass().getResource("main.fxml"))
        );
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}