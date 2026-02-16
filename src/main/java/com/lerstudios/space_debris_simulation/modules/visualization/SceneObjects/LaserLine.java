package com.lerstudios.space_debris_simulation.modules.visualization.SceneObjects;

import com.lerstudios.space_debris_simulation.modules.visualization.VisualizationGraphics;
import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;
import com.lerstudios.space_debris_simulation.utils.Constants;
import com.lerstudios.space_debris_simulation.utils.PropagationTools;
import com.lerstudios.space_debris_simulation.utils.Vector3;
import javafx.scene.paint.Color;
import org.fxyz3d.geometry.Point3D;

import java.util.Arrays;
import java.util.List;

public class LaserLine {
    private PolyLine3D line;
    private Color color;

    public LaserLine(String color) {
        this.color = Color.web(color);
    }

    public void setEndPoints(Vector3 start, Vector3 end, VisualizationGraphics graphics) {
        if (line != null) {
            graphics.removeFromGroup(line);
        }

        if(end == null) {
            end = start;
        }

        List<Point3D> points = Arrays.asList(
                new Point3D(start.x, start.y, start.z),
                new Point3D(end.x, end.y, end.z)
        );

        line = new PolyLine3D(points, 1f, color);
        graphics.addGrouptoGroup(line);
    }
}
