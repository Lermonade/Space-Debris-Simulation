package com.lerstudios.space_debris_simulation;

import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.SimToVis_File;
import com.lerstudios.space_debris_simulation.modules.visualization.Timing;
import com.lerstudios.space_debris_simulation.modules.visualization.VisualizationGraphics;
import com.lerstudios.space_debris_simulation.modules.visualization.VisualizationMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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

    @FXML
    private VBox timeline;


    Timing timing;
    VisualizationGraphics visualization;
    VisualizationMain controller;

    public void initVisualization(SimToVis_File file) {

        visualization = new VisualizationGraphics(rootPane);
        controller = new VisualizationMain(file, visualization);
        timing = new Timing(controller::updateScene, timeDisplay, timeTextDisplay, pausePlayButton, speedButton, slowButton, startButton, endButton);

        controller.initializeVisualization();

    }

    public void switchCam() {
        visualization.switchCam();
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

    boolean timelineActive = true;
    public void toggleTimeline() {
        timelineActive = !timelineActive;
        timeline.setVisible(timelineActive);
        timeline.setManaged(timelineActive);
    }
}