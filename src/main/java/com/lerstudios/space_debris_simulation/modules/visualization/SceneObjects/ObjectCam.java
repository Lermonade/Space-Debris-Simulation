package com.lerstudios.space_debris_simulation.modules.visualization.SceneObjects;

import com.lerstudios.space_debris_simulation.interfaces.Objects.Object3D;
import com.lerstudios.space_debris_simulation.interfaces.Objects.PropagationDataObject;
import com.lerstudios.space_debris_simulation.utils.PropagationTools;
import com.lerstudios.space_debris_simulation.modules.visualization.Timing;
import com.lerstudios.space_debris_simulation.modules.visualization.populations.Objects.Visualization_ObjectRegistry;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;

import java.util.ArrayList;
import java.util.List;

public class ObjectCam {

    private static final List<ObjectCam> cameras = new ArrayList<>();

    private Object3D targetObject;
    private final Camera camera;

    public ObjectCam() {
        this.targetObject = Visualization_ObjectRegistry.getFirstObject();

        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(5000);

        camera.setTranslateX(0);
        camera.setTranslateY(120);
        camera.setTranslateZ(0);

        // Register this camera
        cameras.add(this);
    }

    public void setTargetObject(String name) {
        this.targetObject = Visualization_ObjectRegistry.getObjectByName(name);
    }

    public Camera getCamera() {
        return this.camera;
    }

    public void updateCamera() {
        if (targetObject == null) targetObject = Visualization_ObjectRegistry.getFirstObject();

        double[] pos = addExtension(
                targetObject.getX(),
                targetObject.getY(),
                targetObject.getZ(),
                1
        );

        camera.setTranslateX(pos[0]);
        camera.setTranslateY(pos[1]);
        camera.setTranslateZ(pos[2]);

        rotateCameraAlongVelocity(Timing.getSeconds());
    }

    public static void updateAllCameras() {
        for (ObjectCam cam : cameras) {
            cam.updateCamera();
        }
    }

    private static double[] addExtension(
            double x, double y, double z,
            double extension
    ) {
        double length = Math.sqrt(x*x + y*y + z*z);
        if (length == 0) return new double[] {0, 0, 0}; // cannot define direction

        double dx = x / length;
        double dy = y / length;
        double dz = z / length;

        return new double[] {
                x + dx * extension,
                y + dy * extension,
                z + dz * extension
        };
    }

    public void rotateCameraAlongVelocity(double seconds) {
        if (targetObject == null) targetObject = Visualization_ObjectRegistry.getFirstObject();

        if(targetObject == targetObject) {
            return;
        }

        // 1️⃣ Forward vector = velocity (do NOT flip)
        Point3D forward = PropagationTools.getVelocityVector((PropagationDataObject) targetObject, seconds);
        if (forward.magnitude() == 0) return;
        forward = forward.normalize();

        // 2️⃣ Up vector = from camera to Earth
        Point3D camPos = new Point3D(camera.getTranslateX(), camera.getTranslateY(), camera.getTranslateZ());
        Point3D up = camPos.multiply(-1).normalize(); // points toward Earth

        // 3️⃣ Right vector = forward × up
        Point3D right = forward.crossProduct(up).normalize();

        // 4️⃣ Corrected up = right × forward
        Point3D correctedUp = right.crossProduct(forward).normalize();

        // 5️⃣ Apply main affine
        javafx.scene.transform.Affine affine = new javafx.scene.transform.Affine(
                right.getX(), correctedUp.getX(), forward.getX(), 0,
                right.getY(), correctedUp.getY(), forward.getY(), 0,
                right.getZ(), correctedUp.getZ(), forward.getZ(), 0
        );

        // 6️⃣ Optional tilt downward
        double tiltDegrees = 15;
        affine.prepend(new javafx.scene.transform.Rotate(tiltDegrees, new Point3D(right.getX(), right.getY(), right.getZ())));

        // 7️⃣ Slightly move back along the forward vector to avoid clipping
        double distanceBack = 2;
        camera.setTranslateX(camera.getTranslateX() - forward.getX() * distanceBack);
        camera.setTranslateY(camera.getTranslateY() - forward.getY() * distanceBack);
        camera.setTranslateZ(camera.getTranslateZ() - forward.getZ() * distanceBack);

        // 8️⃣ Set camera transform
        camera.getTransforms().setAll(affine);
    }

}

