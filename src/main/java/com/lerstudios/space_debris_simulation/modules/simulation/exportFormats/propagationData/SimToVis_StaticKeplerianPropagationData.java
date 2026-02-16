package com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData;

import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;

public class SimToVis_StaticKeplerianPropagationData implements SimToVis_PropagationData {
    public RadianKeplerianElements elements;

    public SimToVis_StaticKeplerianPropagationData() {}

    public SimToVis_StaticKeplerianPropagationData(RadianKeplerianElements elements) {
        this.elements = elements;
    }

    @Override
    public Object getPropagationData() {
        return elements;
    }
}
