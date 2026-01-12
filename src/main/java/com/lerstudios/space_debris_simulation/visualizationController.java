package com.lerstudios.space_debris_simulation;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.fxyz3d.geometry.Point3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public void initialize() {
        Timing timing = new Timing(timeDisplay, timeTextDisplay, pausePlayButton, speedButton, slowButton, startButton, endButton);
        VisualizationGraphics visualization = new VisualizationGraphics(rootPane);
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