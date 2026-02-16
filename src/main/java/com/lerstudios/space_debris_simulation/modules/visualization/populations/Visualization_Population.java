package com.lerstudios.space_debris_simulation.modules.visualization.populations;

import com.lerstudios.space_debris_simulation.interfaces.Objects.PropagationDataObject;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PointInterpolationPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.StaticKeplerianPropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.SimUtils;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.objects.SimToVis_ObjectBase;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData.SimToVis_DynamicSwitchingPropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData.SimToVis_PropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData.SimToVis_SimplifiedVelocityVerletPropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData.SimToVis_StaticKeplerianPropagationData;
import com.lerstudios.space_debris_simulation.modules.visualization.Timing;
import com.lerstudios.space_debris_simulation.modules.visualization.VisualizationGraphics;
import com.lerstudios.space_debris_simulation.modules.visualization.populations.Objects.Visualization_PopulationObject;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.VisualizationMethod;
import com.lerstudios.space_debris_simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.types.TimedCoordinates;
import com.lerstudios.space_debris_simulation.utils.Constants;

import java.util.ArrayList;

public class Visualization_Population {
    String name;

    // Propagation and visualization algorithms. NOT utility classes, actual instances, holding data like color.
    PropagationMethod propagator;
    VisualizationMethod visualizer;

    // Changes depending on visualizationType. Holds object data.
    ArrayList<Visualization_PopulationObject> objects = new ArrayList<>();

    public Visualization_Population(String name, PropagationMethod propagator, VisualizationMethod visualizer) {
        this.name = name;
        this.propagator = propagator;
        this.visualizer = visualizer;
    }

    public void addObject(SimToVis_ObjectBase initObject, VisualizationGraphics graphics, double seconds) {
        Visualization_PopulationObject object = new Visualization_PopulationObject(initObject.getName(), initObject.getClassification());
        object.setVisualizationData(this.visualizer.setupObject(object, graphics));
        object.setPropagationData(this.propagator.setupObject(object, createPropagationDataFromObject(initObject)));

        if(initObject.getEndData() != null) {
            object.endData = initObject.getEndData();
        }

        updateObject(object, seconds);

        objects.add(object);
    }

    public PropagationData createPropagationDataFromObject(SimToVis_ObjectBase initObject) {
        SimToVis_PropagationData initPropData = initObject.getPropagationData();

        if(initObject.getPropagationMethod() == PropegationMethod.STATIC_KEPLERIAN) {
            SimToVis_StaticKeplerianPropagationData p2 = (SimToVis_StaticKeplerianPropagationData) initPropData;
            StaticKeplerianPropagationData output = new StaticKeplerianPropagationData();
            output.elements = p2.elements;
            return output;
        } else if(initObject.getPropagationMethod() == PropegationMethod.SIMPLIFIED_VELOCITY_VERLET) {
            SimToVis_SimplifiedVelocityVerletPropagationData p2 = (SimToVis_SimplifiedVelocityVerletPropagationData) initObject.getPropagationData();
            return new PointInterpolationPropagationData(p2.points);
        } else if(initObject.getPropagationMethod() == PropegationMethod.DYNAMIC_SWITCHING) {
            SimToVis_DynamicSwitchingPropagationData p2 = (SimToVis_DynamicSwitchingPropagationData) initObject.getPropagationData();
            return new PointInterpolationPropagationData(p2.points);
        }

        return null;
    }

    public void updateAllObjects(double seconds) {
        for(Visualization_PopulationObject o : objects) {
            updateObject(o, seconds);
        }
    }

    public void updateObject(Visualization_PopulationObject o, double seconds) {
        TimedCoordinates endData = o.endData;
        if(endData != null && seconds >= (endData.timeStep() * SimUtils.getSecondsPerTimeStep())) {
            o.setPosition(endData.x(), endData.y(), endData.z());
        } else {
            propagator.getPositionFromTime(o, seconds);
            visualizer.moveObject(o, seconds);
        }
    }
}
