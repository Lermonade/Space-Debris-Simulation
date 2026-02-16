package com.lerstudios.space_debris_simulation.modules.simulation.populations.populationObjects;

import com.lerstudios.space_debris_simulation.interfaces.Objects.Object3D;
import com.lerstudios.space_debris_simulation.interfaces.Objects.PropagationDataObject;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.types.PropegationMethod;

public interface Simulation_Object extends Object3D, PropagationDataObject {
    PropegationMethod getPropagationMethod();
}