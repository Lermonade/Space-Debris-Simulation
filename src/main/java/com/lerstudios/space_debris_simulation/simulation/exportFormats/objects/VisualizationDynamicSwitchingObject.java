package com.lerstudios.space_debris_simulation.simulation.exportFormats.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lerstudios.space_debris_simulation.simulation.SimUtils;
import com.lerstudios.space_debris_simulation.simulation.types.KeplerianElements;
import com.lerstudios.space_debris_simulation.simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.simulation.types.TimedKeplerianElements;

import java.util.ArrayList;

@JsonIgnoreProperties({ "propagationMethod" })
public class VisualizationDynamicSwitchingObject implements VisualizationObject {
    String name;
    public ArrayList<TimedKeplerianElements> elements = new ArrayList<>();
    ObjectClassification classification;

    public VisualizationDynamicSwitchingObject() {}

    public VisualizationDynamicSwitchingObject(String name, ObjectClassification classification) {
        this.name = name;
        this.classification = classification;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public PropegationMethod getPropagationMethod() {
        return PropegationMethod.DYNAMIC_SWITCHING;
    }

    @Override
    public ObjectClassification getClassification() {
        return this.classification;
    }

    @Override
    public Object getOrbitData() {
        return null;
    }
}
