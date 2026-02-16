package com.lerstudios.space_debris_simulation.modules.simulation.populations.populationObjects;

import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.StaticKeplerianPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;

public class Simulation_StaticKeplerianObject extends Simulation_ObjectBase {
    public Simulation_StaticKeplerianObject(String name, RadianKeplerianElements elements, ObjectClassification classification) {
        super(name, classification, PropegationMethod.STATIC_KEPLERIAN);

        StaticKeplerianPropagationData data = new StaticKeplerianPropagationData();
        data.elements = elements;
        this.setPropagationData(data);
    }
}
