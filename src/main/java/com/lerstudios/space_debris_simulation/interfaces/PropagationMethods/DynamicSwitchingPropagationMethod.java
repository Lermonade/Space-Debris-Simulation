package com.lerstudios.space_debris_simulation.interfaces.PropagationMethods;

import com.lerstudios.space_debris_simulation.interfaces.Objects.PropagationDataObject;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.DynamicSwitchingPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.SimUtils;

public class DynamicSwitchingPropagationMethod implements PropagationMethod {
    @Override
    public void getPositionFromTime(PropagationDataObject object, double seconds) {
        DynamicSwitchingPropagationData data = (DynamicSwitchingPropagationData) object.getPropagationData();

        object.setPosition(object.getX(), seconds, object.getZ());
        if (SimUtils.getCreateVisualizationFile()) {
            data.addVisualizationPosition(SimUtils.getCurrentTimeStep() * SimUtils.getSecondsPerTimeStep(), object.getPosition());
        }
    }

    @Override
    public PropagationData setupObject(PropagationDataObject object, PropagationData data) {
        return data;
    }
}
