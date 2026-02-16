package com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData;

import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;
import com.lerstudios.space_debris_simulation.utils.Vector3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicSwitchingPropagationData implements PropagationData {
    public RadianKeplerianElements elements;
    private Map<Double, Vector3> visualizationPositions = new ConcurrentHashMap<>();

    public DynamicSwitchingPropagationData() {}

    public DynamicSwitchingPropagationData(RadianKeplerianElements elements) {
        this.elements = elements;
    }

    public Map<Double, Vector3> getVisualizationPositions() {
        return visualizationPositions;
    }

    public void addVisualizationPosition(double time, Vector3 position) {
        visualizationPositions.put(time, new Vector3(position.x, position.y, position.z));
    }

    @Override
    public Object getPropagationData() {
        return elements;
    }
}
