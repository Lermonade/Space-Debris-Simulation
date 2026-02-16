package com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData;

import com.lerstudios.space_debris_simulation.utils.Vector3;

import java.util.Map;

public class SimToVis_DynamicSwitchingPropagationData implements SimToVis_PropagationData {
    public Map<Double, Vector3> points;

    public SimToVis_DynamicSwitchingPropagationData() {}

    public SimToVis_DynamicSwitchingPropagationData(Map<Double, Vector3> points) {
        this.points = points;
    }

    @Override
    public Object getPropagationData() {
        return points;
    }
}
