package com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData;

import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;
import com.lerstudios.space_debris_simulation.utils.Vector3;

import java.util.Map;

public class SimToVis_SimplifiedVelocityVerletPropagationData implements SimToVis_PropagationData {
    public Map<Double, Vector3> points;

    public SimToVis_SimplifiedVelocityVerletPropagationData() {}

    public SimToVis_SimplifiedVelocityVerletPropagationData(Map<Double, Vector3> points) {
        this.points = points;
    }

    @Override
    public Object getPropagationData() {
        return points;
    }
}
