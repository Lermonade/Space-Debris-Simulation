package com.lerstudios.space_debris_simulation.interfaces.Objects.propagatableObjects;

import com.lerstudios.space_debris_simulation.interfaces.Objects.PropagationDataObject;
import com.lerstudios.space_debris_simulation.types.KeplerianElements;

public interface StaticKeplerianPropagationObject extends PropagationDataObject {
    KeplerianElements getOrbitData();
}
