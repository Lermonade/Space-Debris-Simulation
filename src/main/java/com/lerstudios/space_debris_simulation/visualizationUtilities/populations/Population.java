package com.lerstudios.space_debris_simulation.visualizationUtilities.populations;

import com.lerstudios.space_debris_simulation.simulation.exportFormats.VisualizationPopulation;
import com.lerstudios.space_debris_simulation.simulation.exportFormats.objects.VisualizationObject;
import com.lerstudios.space_debris_simulation.visualizationUtilities.VisualizationGraphics;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.Basic3DObject;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.Object;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.VisualizationMethods.VisualizationMethod;

import java.util.ArrayList;

public class Population {
    String name;

    // Propagation and visualization algorithms. NOT utility classes, actual instances, holding data like color.
    PropagationMethod propagator;
    VisualizationMethod visualizer;

    // Changes depending on visualizationType. Holds object data.
    ArrayList<Object> objects = new ArrayList<>();

    public Population(String name, PropagationMethod propagator, VisualizationMethod visualizer) {
        this.name = name;
        this.propagator = propagator;
        this.visualizer = visualizer;
    }

    public void addObject(VisualizationObject initObject, VisualizationGraphics graphics, double seconds) {
        Object object = new Basic3DObject(initObject.getName(), initObject.getClassification());
        object.setVisualizationData(this.visualizer.setupObject(object, graphics));
        object.setPropagationData(this.propagator.setupObject(object, initObject));

        updateObject(object, seconds);

        objects.add(object);
    }

    public void updateAllObjects(double seconds) {
        for(Object o : objects) {
            updateObject(o, seconds);
        }
    }

    public void updateObject(Object o, double seconds) {
        propagator.getPositionFromTime(o, seconds);
        visualizer.moveObject(o, seconds);
    }
}
