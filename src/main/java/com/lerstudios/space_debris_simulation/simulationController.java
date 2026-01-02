package com.lerstudios.space_debris_simulation;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class simulationController {

    @FXML
    private AnchorPane rootPane;

    public void initialize() {
        Group group = new Group();

        // Objects
        initializeEarth(group);

        // Lighting
        AmbientLight ambient = new AmbientLight(Color.rgb(30, 30, 30));
        group.getChildren().add(ambient);

        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(1000);
        light.setTranslateY(0);
        light.setTranslateZ(0);
        group.getChildren().add(light);

        // Scene Setup
        SubScene window3D = new SubScene(group, 800, 600, true, SceneAntialiasing.BALANCED);
        window3D.setFill(Color.BLACK);

        // Scene Size Binding
        window3D.widthProperty().bind(rootPane.widthProperty());
        window3D.heightProperty().bind(rootPane.heightProperty());

        // Camera Setup Functions
        setupCamera(window3D);

        // Add the Scene
        rootPane.getChildren().add(window3D);
    }

    public void initializeEarth(Group group) {
        Sphere sphere = new Sphere(100);

        // Color Texture
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lerstudios/space_debris_simulation/assets/textures/earth_color.png"))));

        sphere.setMaterial(material);
        group.getChildren().add(sphere);
    }

    public void setupCamera(SubScene window3D) {
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(5000);

        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

        Translate translateZ = new Translate(0, 0, -200);

        camera.getTransforms().addAll(
                rotateY,
                rotateX,
                translateZ
        );

        final double[] anchorX = new double[1];
        final double[] anchorY = new double[1];

        window3D.setOnMousePressed(e -> {
            anchorX[0] = e.getSceneX();
            anchorY[0] = e.getSceneY();
        });

        window3D.setOnMouseDragged(e -> {
            double deltaX = e.getSceneX() - anchorX[0];
            double deltaY = e.getSceneY() - anchorY[0];

            rotateY.setAngle(rotateY.getAngle() + deltaX * 0.5);
            rotateX.setAngle(rotateX.getAngle() - deltaY * 0.5);

            // Clamp pitch so camera doesn't flip
            rotateX.setAngle(Math.max(-90, Math.min(90, rotateX.getAngle())));

            anchorX[0] = e.getSceneX();
            anchorY[0] = e.getSceneY();
        });

        window3D.setCamera(camera);

        // Events
        Platform.runLater(() -> {
            Stage stage = (Stage) window3D.getScene().getWindow();

            stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                switch (event.getCode()) {
                    case W:
                        camera.translateZProperty().set(camera.getTranslateZ() + 10);
                        break;
                    case S:
                        camera.translateZProperty().set(camera.getTranslateZ() - 10);
                        break;
                    default:
                        break;
                }
            });

            window3D.setOnScroll(event -> {
                double zoomSpeed = 1.0;

                double newZ = translateZ.getZ() + event.getDeltaY() * zoomSpeed;

                // Clamp zoom distance
                newZ = Math.max(-1000, Math.min(-102, newZ));

                translateZ.setZ(newZ);
            });

        });
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