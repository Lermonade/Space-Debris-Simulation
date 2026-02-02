package com.lerstudios.space_debris_simulation.simulation.Propagation.PopulationObjects;

import com.lerstudios.space_debris_simulation.simulation.types.KeplerianElements;
import com.lerstudios.space_debris_simulation.simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.simulation.types.PropegationMethod;

public class StaticKeplerianObject implements OrbitalObject {
    String name;
    public KeplerianElements elements;
    boolean propagating = true;
    ObjectClassification classification;

    public StaticKeplerianObject(String name, KeplerianElements elements, ObjectClassification classification) {
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
}
