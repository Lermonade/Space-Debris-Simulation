package com.lerstudios.space_debris_simulation.simulation.exportFormats.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lerstudios.space_debris_simulation.simulation.types.KeplerianElements;
import com.lerstudios.space_debris_simulation.simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.simulation.types.PropegationMethod;

@JsonIgnoreProperties({ "propagationMethod" })
public class VisualizationStaticKeplerianObject implements VisualizationObject {
    String name;
    public KeplerianElements elements;
    boolean propagating = true;
    ObjectClassification classification;

    public VisualizationStaticKeplerianObject() {}

    public VisualizationStaticKeplerianObject(String name, KeplerianElements elements, ObjectClassification classification) {
        this.name = name;
        this.elements = elements;
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
    public ObjectClassification getClassification() {
        return this.classification;
    }

    public KeplerianElements getOrbitData() {
        return this.elements;
    }
}
