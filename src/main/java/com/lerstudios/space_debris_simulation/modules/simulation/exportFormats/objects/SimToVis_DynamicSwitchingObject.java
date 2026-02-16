package com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData.SimToVis_DynamicSwitchingPropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData.SimToVis_PropagationData;
import com.lerstudios.space_debris_simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.types.TimedCoordinates;
import com.lerstudios.space_debris_simulation.types.TimedKeplerianElements;

import java.util.ArrayList;

@JsonIgnoreProperties({ "propagationMethod" })
public class SimToVis_DynamicSwitchingObject implements SimToVis_ObjectBase {
    String name;
    ObjectClassification classification;
    public TimedCoordinates endData = null;

    public SimToVis_PropagationData data;

    public SimToVis_DynamicSwitchingObject() {}

    public SimToVis_DynamicSwitchingObject(String name, ObjectClassification classification, SimToVis_DynamicSwitchingPropagationData data) {
        this.name = name;
        this.classification = classification;
        this.data = data;
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
    @JsonIgnore
    public SimToVis_PropagationData getPropagationData() {
        return data;
    }

    @Override
    public TimedCoordinates getEndData() {
        return endData;
    }

    @Override
    public ObjectClassification getClassification() {
        return this.classification;
    }
}
