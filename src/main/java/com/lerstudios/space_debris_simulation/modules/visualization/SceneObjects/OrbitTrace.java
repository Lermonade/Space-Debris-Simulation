package com.lerstudios.space_debris_simulation.modules.visualization.SceneObjects;

import com.lerstudios.space_debris_simulation.utils.PropagationTools;
import com.lerstudios.space_debris_simulation.modules.visualization.VisualizationGraphics;
import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;
import com.lerstudios.space_debris_simulation.utils.Constants;
import javafx.scene.paint.Color;
import org.fxyz3d.geometry.Point3D;

import java.util.List;

public class OrbitTrace {
    private PolyLine3D line;
    int lineResolution = 40;
    private Color color;

    public OrbitTrace(String color) {
        this.color = Color.web(color);
    }

    public void createOrbitalPath(RadianKeplerianElements elements, VisualizationGraphics graphics) {

        if (line != null) {
            graphics.removeFromGroup(line);
        }

        List<Point3D> points = PropagationTools.generateOrbitPoints(
                0, 0, 0,
                elements.semiMajorAxis() * Constants.scaleFactor,
                elements.eccentricity(),
                elements.argPeriapsis(),
                elements.inclination(),
                elements.raan(),
                elements.anomaly(),
                Constants.gravitationalConstant * Constants.earthMass,
                this.lineResolution,
                true
        );

        line = new PolyLine3D(points, 1f, color);
        graphics.addGrouptoGroup(line);
    }
}
