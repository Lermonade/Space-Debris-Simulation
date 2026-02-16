package com.lerstudios.space_debris_simulation.modules.visualization.populations.RemovalMethods;

import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PointInterpolationPropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.StaticKeplerianPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.StaticKeplerianPropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.Object3DVisualizationMethod;
import com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.VisualizationMethod;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData.SimToVis_StaticKeplerianPropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.removalMethods.SimToVis_OrbitalLaser;
import com.lerstudios.space_debris_simulation.modules.visualization.SceneObjects.LaserLine;
import com.lerstudios.space_debris_simulation.modules.visualization.SceneObjects.OrbitTrace;
import com.lerstudios.space_debris_simulation.modules.visualization.Timing;
import com.lerstudios.space_debris_simulation.modules.visualization.VisualizationGraphics;
import com.lerstudios.space_debris_simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;
import com.lerstudios.space_debris_simulation.utils.Constants;
import com.lerstudios.space_debris_simulation.utils.Vector3;

import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class Visualization_OrbitalLaser extends Visualization_RemovalMethod {

    OrbitTrace trace;;
    VisualizationGraphics graphics;
    boolean showLine = false;

    LaserLine laser;

    Map<Double, Vector3> laserTargetPositions;

    public Visualization_OrbitalLaser(
                        PropagationMethod propagator,
                        VisualizationMethod visualizer,
                        VisualizationGraphics graphics,
                        SimToVis_OrbitalLaser initObject,
                        double seconds) {

        super(initObject.name, initObject.getPropagationMethod(), propagator, visualizer, ObjectClassification.REMOVAL_METHOD, graphics, initObject, seconds);
        this.graphics = graphics;
        trace = new OrbitTrace(initObject.color);
        this.showLine = initObject.renderLines;
        this.laserTargetPositions = initObject.laserTargetPoints;

        laser = new LaserLine(initObject.laserColor);

        if(initObject.endData != null) {
            this.endData = initObject.endData;
        }

        updateObject(seconds);
    }

    public static Visualization_OrbitalLaser build(
            SimToVis_OrbitalLaser initObject, VisualizationGraphics graphics, double seconds) {
        PropegationMethod method = initObject.getPropagationMethod();
        PropagationMethod propagator = null;

        if(method == PropegationMethod.STATIC_KEPLERIAN) {
            propagator = new StaticKeplerianPropagationMethod(false, true);
        } else if(method == PropegationMethod.SIMPLIFIED_VELOCITY_VERLET) {
            propagator = new PointInterpolationPropagationMethod();
        }
        Object3DVisualizationMethod visualizer = new Object3DVisualizationMethod(initObject.color, 1.5);

        Visualization_OrbitalLaser output = new Visualization_OrbitalLaser(propagator, visualizer, graphics, initObject, seconds);

        return output;
    }

    @Override
    public void updateObject(double seconds) {
        // First, call the parent implementation
        super.updateObject(seconds);

        if(this.showLine) {
            if(this.getPropagationMethod() == PropegationMethod.STATIC_KEPLERIAN) {
                StaticKeplerianPropagationMethod p2 = (StaticKeplerianPropagationMethod) propagator;
                RadianKeplerianElements e = p2.getKeplerianData(this);

                this.trace.createOrbitalPath(e, graphics);
            }
        }

        if(this.endData != null) {
            if(seconds >= (endData.timeStep() * Timing.secondsPerTimeStep)) {
                laser.setEndPoints(new Vector3(this.getX(), this.getY(), this.getZ()), null, graphics);
                return;
            }
        }

        Vector3 endPos = getLaserEndPos(seconds);
        laser.setEndPoints(new Vector3(this.getX(), this.getY(), this.getZ()),
                endPos, graphics);
    }

    private Vector3 getLaserEndPos(double seconds) {
        if (laserTargetPositions == null || laserTargetPositions.isEmpty()) {
            return null; // nothing targeted
        }

        // Use a TreeMap for easy floor/ceiling
        TreeMap<Double, Vector3> sorted = new TreeMap<>(laserTargetPositions);

        // Clamp to bounds
        if (seconds <= sorted.firstKey()) {
            Vector3 p = sorted.firstEntry().getValue();
            if (p == null) return null;
            return new Vector3(p.x * Constants.scaleFactor,
                    p.y * Constants.scaleFactor,
                    p.z * Constants.scaleFactor);
        }
        if (seconds >= sorted.lastKey()) {
            Vector3 p = sorted.lastEntry().getValue();
            if (p == null) return null;
            return new Vector3(p.x * Constants.scaleFactor,
                    p.y * Constants.scaleFactor,
                    p.z * Constants.scaleFactor);
        }

        // Find neighbors
        Map.Entry<Double, Vector3> lower = sorted.floorEntry(seconds);
        Map.Entry<Double, Vector3> upper = sorted.ceilingEntry(seconds);

        if (lower == null || upper == null) return null;

        Vector3 p0 = lower.getValue();
        Vector3 p1 = upper.getValue();

        if (p0 == null || p1 == null) return null; // one of the neighbors is null

        double t0 = lower.getKey();
        double t1 = upper.getKey();

        if (t0 == t1) {
            // exact match
            return new Vector3(p0.x * Constants.scaleFactor,
                    p0.y * Constants.scaleFactor,
                    p0.z * Constants.scaleFactor);
        }

        // Linear interpolation
        double alpha = (seconds - t0) / (t1 - t0);

        double x = p0.x + (p1.x - p0.x) * alpha;
        double y = p0.y + (p1.y - p0.y) * alpha;
        double z = p0.z + (p1.z - p0.z) * alpha;

        return new Vector3(x * Constants.scaleFactor,
                y * Constants.scaleFactor,
                z * Constants.scaleFactor);
    }

}
