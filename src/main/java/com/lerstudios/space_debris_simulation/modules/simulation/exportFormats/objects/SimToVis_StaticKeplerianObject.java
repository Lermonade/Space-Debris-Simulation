package com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData.SimToVis_PropagationData;
import com.lerstudios.space_debris_simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;
import com.lerstudios.space_debris_simulation.types.TimedCoordinates;

@JsonIgnoreProperties({ "propagationMethod" })
public class SimToVis_StaticKeplerianObject implements SimToVis_ObjectBase {
    String name;
    boolean propagating = true;
    ObjectClassification classification;
    public TimedCoordinates endData = null;

    @JsonProperty("propagationData")
    SimToVis_PropagationData data;

    public SimToVis_StaticKeplerianObject() {}

    public SimToVis_StaticKeplerianObject(String name, SimToVis_PropagationData data, ObjectClassification classification) {
        this.name = name;
        this.data = data;
        this.classification = classification;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public PropegationMethod getPropagationMethod() {
        return PropegationMethod.STATIC_KEPLERIAN;
    }

    @Override
    public SimToVis_PropagationData getPropagationData() {
        return data;
    }

    @Override
    public ObjectClassification getClassification() {
        return this.classification;
    }

    @Override
    public TimedCoordinates getEndData() {
        return endData;
    }
}
