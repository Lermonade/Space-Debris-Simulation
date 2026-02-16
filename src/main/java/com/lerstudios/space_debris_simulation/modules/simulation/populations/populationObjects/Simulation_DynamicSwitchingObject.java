package com.lerstudios.space_debris_simulation.modules.simulation.populations.populationObjects;

import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.DynamicSwitchingPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.StaticKeplerianPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.types.*;
import com.lerstudios.space_debris_simulation.modules.simulation.SimUtils;

import java.util.ArrayList;

public class Simulation_DynamicSwitchingObject extends Simulation_ObjectBase {

    public Simulation_DynamicSwitchingObject(String name, ObjectClassification classification, RadianKeplerianElements elements) {
        super(name, classification, PropegationMethod.DYNAMIC_SWITCHING);

        DynamicSwitchingPropagationData data = new DynamicSwitchingPropagationData();
        data.elements = elements;
        this.setPropagationData(data);
    }
}
