package com.lerstudios.space_debris_simulation;

import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.fxyz3d.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VisualizationGraphics {

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

    private Group group;

    public VisualizationGraphics(AnchorPane rootPane) {
        initialize3D(rootPane);
    }

    private void initialize3D(AnchorPane rootPane) {
        group = new Group();

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

    private void initializeEarth(Group group) {
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

    private void initializeSkybox(Group group, Camera camera) {
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

    private Camera setupCamera(SubScene window3D) {
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

    public void add3DObjectToGroup(Shape3D object) {
        if (this.group == null || object == null) return;
        this.group.getChildren().add(object);
    }

    public void addGrouptoGroup(Group group2) {
        if (this.group == null || group2 == null) return;
        this.group.getChildren().add(group2);
    }

}
