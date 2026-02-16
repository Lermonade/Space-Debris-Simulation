package com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData;

import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;
import com.lerstudios.space_debris_simulation.utils.Vector3;

import java.util.Map;

public class PointInterpolationPropagationData implements PropagationData {
    public Map<Double, Vector3> points;

    public PointInterpolationPropagationData() {}

    public PointInterpolationPropagationData(Map<Double, Vector3> points) {
        this.points = points;
    }

    @Override
    public Object getPropagationData() {
        return points;
    }
}
