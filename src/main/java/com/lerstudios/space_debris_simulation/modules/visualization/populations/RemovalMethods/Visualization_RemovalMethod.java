package com.lerstudios.space_debris_simulation.modules.visualization.populations.RemovalMethods;

import com.lerstudios.space_debris_simulation.interfaces.Objects.PropagationDataObject;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PointInterpolationPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.StaticKeplerianPropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.SimUtils;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData.SimToVis_SimplifiedVelocityVerletPropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData.SimToVis_StaticKeplerianPropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.removalMethods.SimToVis_OrbitalLaser;
import com.lerstudios.space_debris_simulation.modules.visualization.Timing;
import com.lerstudios.space_debris_simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.modules.visualization.VisualizationGraphics;
import com.lerstudios.space_debris_simulation.modules.visualization.populations.Objects.Visualization_PopulationObject;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.VisualizationMethod;
import com.lerstudios.space_debris_simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;
import com.lerstudios.space_debris_simulation.types.TimedCoordinates;

public class Visualization_RemovalMethod extends Visualization_PopulationObject {
    PropagationMethod propagator;
    VisualizationMethod visualizer;

    PropegationMethod propegationMethod;

    public TimedCoordinates endData = null;

    public Visualization_RemovalMethod(String name, PropegationMethod method, PropagationMethod propagator, VisualizationMethod visualizer, ObjectClassification classification, VisualizationGraphics graphics, SimToVis_OrbitalLaser initObject, double seconds) {
        super(name, classification);

        this.propagator = propagator;
        this.visualizer = visualizer;
        this.propegationMethod = method;

        PropagationData data = null;

        if(initObject.getPropagationMethod() == PropegationMethod.STATIC_KEPLERIAN) {
            SimToVis_StaticKeplerianPropagationData p2 = (SimToVis_StaticKeplerianPropagationData) initObject.getPropagationData();
            data = new StaticKeplerianPropagationData(p2.elements);
        } else if(initObject.getPropagationMethod() == PropegationMethod.SIMPLIFIED_VELOCITY_VERLET) {
            SimToVis_SimplifiedVelocityVerletPropagationData p2 = (SimToVis_SimplifiedVelocityVerletPropagationData) initObject.getPropagationData();
            data = new PointInterpolationPropagationData(p2.points);
        }

        this.setVisualizationData(this.visualizer.setupObject(this, graphics));
        this.setPropagationData(this.propagator.setupObject(this, data));
    }

    public void updateObject(double seconds) {
        if(endData != null && seconds >= (endData.timeStep() * Timing.secondsPerTimeStep)) {
            this.setPosition(endData.x(), endData.y(), endData.z());
        } else {
            propagator.getPositionFromTime(this, seconds);
            visualizer.moveObject(this, seconds);
        }
    }

    public PropegationMethod getPropagationMethod() {
        return this.propegationMethod;
    }
}
