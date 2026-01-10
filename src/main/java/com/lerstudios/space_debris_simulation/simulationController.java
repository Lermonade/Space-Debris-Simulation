package com.lerstudios.space_debris_simulation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.fxyz3d.geometry.Point3D;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class simulationController {

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

        Group group = new Group();

        // Lighting
        //AmbientLight ambient = new AmbientLight(Color.rgb(30, 30, 30));
        //group.getChildren().add(ambient);

        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(1000);
        light.setTranslateY(0);
        light.setTranslateZ(0);
        group.getChildren().add(light);

        PointLight light2 = new PointLight(Color.rgb(30, 30, 30));
        light2.setTranslateX(-1000);
        light2.setTranslateY(0);
        light2.setTranslateZ(0);
        group.getChildren().add(light2);

        PolyLine3D line = new PolyLine3D(
                generateEllipsePoints(0, 0, 0, 200, 0.3, 0, Math.PI/4, Math.PI/2, 20),
                1f,
                Color.WHITE
        );
        group.getChildren().add(line);

        // Scene Setup
        SubScene window3D = new SubScene(group, 800, 600, true, SceneAntialiasing.BALANCED);
        window3D.setFill(Color.BLACK);

        // Scene Size Binding
        window3D.widthProperty().bind(rootPane.widthProperty());
        window3D.heightProperty().bind(rootPane.heightProperty());

        // Camera Setup Functions
        Camera camera = setupCamera(window3D);

        // Objects
        initializeEarth(group);
        initializeSkybox(group, camera);

        // Add the Scene
        rootPane.getChildren().add(window3D);
    }

    public void initializeEarth(Group group) {
        Sphere sphere = new Sphere(100);

        // Color Texture
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lerstudios/space_debris_simulation/assets/textures/earth_color.png")),
                4096,
                2048,
                true,
                false));
        material.setSpecularMap(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lerstudios/space_debris_simulation/assets/textures/earth_specular.png")),
                4096,
                2048,
                true,
                false));
        material.setSelfIlluminationMap(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lerstudios/space_debris_simulation/assets/textures/earth_illumination.png")),
                4096,
                2048,
                true,
                false));

        sphere.setMaterial(material);
        group.getChildren().add(sphere);
    }

    public void initializeSkybox(Group group, Camera camera) {
        Sphere skySphere = new Sphere(2500);
        skySphere.setCullFace(CullFace.FRONT);

        // Color Texture
        PhongMaterial material = new PhongMaterial();
        material.setSelfIlluminationMap(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/lerstudios/space_debris_simulation/assets/textures/skybox2.png")),
                4096,
                2048,
                true,
                false));

        camera.localToSceneTransformProperty().addListener((obs, oldT, t) -> {
            skySphere.setTranslateX(t.getTx());
            skySphere.setTranslateY(t.getTy());
            skySphere.setTranslateZ(t.getTz());
        });


        skySphere.setMaterial(material);
        group.getChildren().add(skySphere);
    }

    // Generate an emissive skybox with fully black bg
    // Add 3D explaination with txsef drawings in the notebook
    // Add 3D visualization timeline in the notebook by showing one-step-at-a-time
    // Credits:
    // https://drive.google.com/drive/folders/1MRwpedip8EJVlm7YZi7lvEajPPN6lBwj
    // https://science.nasa.gov/earth/earth-observatory/
    // http://alexcpeterson.com/spacescape/
    // https://www.youtube.com/watch?v=TRJ0GqDBDac&t=2s
    // https://www.youtube.com/watch?v=9XJicRt_FaI&t=13535s
    // https://github.com/FXyz/FXyz/blob/master/FXyz-Core/src/main/java/org/fxyz3d/shapes/composites/PolyLine3D.java

    public Camera setupCamera(SubScene window3D) {
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

        return camera;
    }

    public static List<Point3D> generateEllipsePoints(
            int focusX,
            int focusY,
            int focusZ,
            double semiMajorAxis,        // a
            double eccentricity,         // e (0 <= e < 1)
            double argumentOfPeriapsis,  // ω (radians)
            double inclination,          // i (radians)
            double raOfAscendingNode,    // Ω (radians)
            int resolution
    ) {
        if (eccentricity < 0 || eccentricity >= 1) {
            throw new IllegalArgumentException("Eccentricity must be in range [0, 1).");
        }

        List<Point3D> points = new ArrayList<>();

        double a = semiMajorAxis;
        double b = a * Math.sqrt(1 - eccentricity * eccentricity);
        double c = a * eccentricity;

        double cosW = Math.cos(argumentOfPeriapsis);
        double sinW = Math.sin(argumentOfPeriapsis);

        double cosI = Math.cos(inclination);
        double sinI = Math.sin(inclination);

        double cosO = Math.cos(raOfAscendingNode);
        double sinO = Math.sin(raOfAscendingNode);

        for (int i = 0; i < resolution; i++) {
            double angle = 2 * Math.PI * i / resolution;

            // --- 1. Ellipse in its own plane (focus-based, periapsis on +X)
            double x = a * Math.cos(angle) + c;
            double z = b * Math.sin(angle);
            double y = 0;

            // --- 2. Rotate by argument of periapsis (ω) in orbital plane
            double x1 =  x * cosW - z * sinW;
            double z1 =  x * sinW + z * cosW;
            double y1 =  y;

            // --- 3. Apply inclination (i) — rotate about X axis
            double x2 = x1;
            double y2 = y1 * cosI - z1 * sinI;
            double z2 = y1 * sinI + z1 * cosI;

            // --- 4. Apply RA of ascending node (Ω) — rotate about Y axis
            double x3 =  x2 * cosO - z2 * sinO;
            double z3 =  x2 * sinO + z2 * cosO;
            double y3 =  y2;

            // --- 5. Translate to focus position
            int worldX = focusX + (int) Math.round(x3);
            int worldY = focusY + (int) Math.round(y3);
            int worldZ = focusZ + (int) Math.round(z3);

            points.add(new Point3D(worldX, worldY, worldZ));
        }

        points.add(points.getFirst()); // close the ellipse
        return points;
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