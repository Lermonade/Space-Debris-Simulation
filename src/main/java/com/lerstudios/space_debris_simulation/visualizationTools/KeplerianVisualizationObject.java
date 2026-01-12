package com.lerstudios.space_debris_simulation.visualizationTools;

import com.lerstudios.space_debris_simulation.Constants;
import com.lerstudios.space_debris_simulation.PolyLine3D;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import org.fxyz3d.geometry.Point3D;

public class KeplerianVisualizationObject {
    private static final Color color = Color.RED;

    private Sphere object;
    private PolyLine3D line;

    static final PhongMaterial SHARED_MATERIAL = createSharedMaterial();

    double focusX = 0;
    double focusY = 0;
    double focusZ = 0;
    double semiMajorAxis = 6771000;
    double eccentricity = 0.03;
    double argumentOfPeriapsis = 0;
    double inclination = Math.toRadians(51.6);
    double raOfAscendingNode = 0;
    private double initialTrueAnomaly = 0;

    int lineResolution = 40;

    public KeplerianVisualizationObject(double semiMajorAxis, double eccentricity, double argumentOfPeriapsis, double inclination, double raOfAscendingNode, double initialTrueAnomaly) {
        this.semiMajorAxis = semiMajorAxis;
        this.eccentricity = eccentricity;
        this.argumentOfPeriapsis = argumentOfPeriapsis;
        this.inclination = inclination;
        this.raOfAscendingNode = raOfAscendingNode;
        this.initialTrueAnomaly = initialTrueAnomaly;

        object = new Sphere(0.2, 4);
        object.setMaterial(SHARED_MATERIAL);

        object.setTranslateX(0);
        object.setTranslateY(0);
        object.setTranslateZ(0);
    }

    public Sphere get3DObject() {
        return object;
    }

    public void createOrbitalPath() {
        this.line = new PolyLine3D(
            PropagationTools.generateOrbitPoints(this.focusX, this.focusY, this.focusZ,
                    this.semiMajorAxis * Constants.scaleFactor, this.eccentricity, this.argumentOfPeriapsis,
                    this.inclination, this.raOfAscendingNode, this.lineResolution, true),
            0.05f,
            Color.WHITE
        );
    }

    public void moveToOrbitalPosition(double seconds) {
        Point3D pos = PropagationTools.propagateOrbit(this.focusX, this.focusY, this.focusZ,
                this.semiMajorAxis, this.eccentricity, this.argumentOfPeriapsis, this.inclination, this.raOfAscendingNode,
                this.initialTrueAnomaly, seconds, Constants.earthGravity, true);
        this.object.setTranslateX(pos.getX() * Constants.scaleFactor);
        this.object.setTranslateY(pos.getY() * Constants.scaleFactor);
        this.object.setTranslateZ(pos.getZ() * Constants.scaleFactor);

    }

    public PolyLine3D getPathObject() {
        return this.line;
    }

    private static PhongMaterial createSharedMaterial() {
        WritableImage redEmissive = new WritableImage(1, 1);
        redEmissive.getPixelWriter().setColor(0, 0, KeplerianVisualizationObject.color);

        PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED); // base color
        redMaterial.setSelfIlluminationMap(redEmissive); // glow map
        return redMaterial;
    }
}
