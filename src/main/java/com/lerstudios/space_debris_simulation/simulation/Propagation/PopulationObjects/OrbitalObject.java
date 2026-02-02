package com.lerstudios.space_debris_simulation.simulation.Propagation.PopulationObjects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lerstudios.space_debris_simulation.simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.simulation.types.PropegationMethod;

public interface OrbitalObject {
    String getName();
    PropegationMethod getPropagationMethod();
    ObjectClassification getClassification();
}