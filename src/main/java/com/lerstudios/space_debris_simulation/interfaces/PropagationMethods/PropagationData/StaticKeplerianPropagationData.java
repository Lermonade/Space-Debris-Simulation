package com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData;

import com.lerstudios.space_debris_simulation.types.KeplerianElements;
import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;

public class StaticKeplerianPropagationData implements PropagationData {
    public RadianKeplerianElements elements;

    public StaticKeplerianPropagationData() {}

    public StaticKeplerianPropagationData(RadianKeplerianElements elements) {
        this.elements = elements;
    }

    @Override
    public Object getPropagationData() {
        return elements;
    }
}
