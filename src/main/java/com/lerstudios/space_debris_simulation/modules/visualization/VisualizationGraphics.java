package com.lerstudios.space_debris_simulation.modules.visualization;

import com.lerstudios.space_debris_simulation.modules.visualization.SceneObjects.ObjectCam;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.Random;

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

    boolean orbitCamActive = true;
    private Camera orbitCam;
    private ObjectCam objectCam;
    private SubScene window3D;

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

        //this.addManyParticles();

        // Scene Setup
        window3D = new SubScene(group, 800, 600, true, SceneAntialiasing.BALANCED);
        window3D.setFill(Color.BLACK);

        // Scene Size Binding
        window3D.widthProperty().bind(rootPane.widthProperty());
        window3D.heightProperty().bind(rootPane.heightProperty());

        // Camera Setup Functions
        orbitCam = setupCamera(window3D);

        objectCam = new ObjectCam();

        // Objects
        initializeEarth(group);
        initializeSkybox(group, orbitCam);

        // Add the Scene
        rootPane.getChildren().add(window3D);
    }

    public void switchCam() {
        if(orbitCamActive) {
            window3D.setCamera(objectCam.getCamera());
        } else {
            window3D.setCamera(orbitCam);
        }

        orbitCamActive = !orbitCamActive;
    }

    private void initializeEarth(Group group) {
        Sphere sphere = new Sphere(100, 100);

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
                newZ = Math.max(-4000, Math.min(-102, newZ));

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

    public void addManyParticles() {
        int numParticles = 500_000;
        float particleSize = 0.1f; // half-size → full size = 10
        float halfRange = 100f;  // positions from -300 to +300

        TriangleMesh mesh = new TriangleMesh();

        float[] points = new float[numParticles * 4 * 3]; // 4 vertices per particle, 3 coords
        int p = 0;

        Random random = new Random();

        for (int i = 0; i < numParticles; i++) {
            float x = random.nextFloat() * 2 * halfRange - halfRange; // -300 to +300
            float y = random.nextFloat() * 2 * halfRange - halfRange;
            float z = random.nextFloat() * 2 * halfRange - halfRange;

            // 4 vertices per particle
            points[p++] = x - particleSize; points[p++] = y - particleSize; points[p++] = z;
            points[p++] = x + particleSize; points[p++] = y - particleSize; points[p++] = z;
            points[p++] = x - particleSize; points[p++] = y + particleSize; points[p++] = z;
            points[p++] = x + particleSize; points[p++] = y + particleSize; points[p++] = z;
        }

        mesh.getPoints().setAll(points);

        // dummy texture coords
        mesh.getTexCoords().setAll(0, 0);

        // faces (2 triangles per particle)
        int[] faces = new int[numParticles * 6 * 2]; // 6 indices per triangle, 2 triangles per particle
        int f = 0;
        for (int i = 0; i < numParticles; i++) {
            int base = i * 4;
            // triangle 1
            faces[f++] = base + 0; faces[f++] = 0;
            faces[f++] = base + 1; faces[f++] = 0;
            faces[f++] = base + 2; faces[f++] = 0;
            // triangle 2
            faces[f++] = base + 2; faces[f++] = 0;
            faces[f++] = base + 1; faces[f++] = 0;
            faces[f++] = base + 3; faces[f++] = 0;
        }
        mesh.getFaces().setAll(faces);

        MeshView view = new MeshView(mesh);

        // emissive red material
        WritableImage redEmissive = new WritableImage(1, 1);
        redEmissive.getPixelWriter().setColor(0, 0, Color.RED);

        PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSelfIlluminationMap(redEmissive);

        view.setMaterial(redMaterial);
        view.setCullFace(CullFace.NONE);

        this.group.getChildren().add(view);
    }

    public void removeFromGroup(Node node) {
        this.group.getChildren().remove(node);
    }

}
