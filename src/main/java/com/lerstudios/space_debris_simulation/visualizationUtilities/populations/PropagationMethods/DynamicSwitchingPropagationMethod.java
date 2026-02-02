package com.lerstudios.space_debris_simulation.visualizationUtilities.populations.PropagationMethods;

import com.lerstudios.space_debris_simulation.simulation.exportFormats.objects.VisualizationObject;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.Object;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.PropagationData.PropagationData;

public class DynamicSwitchingPropagationMethod implements PropagationMethod {
    @Override
    public void getPositionFromTime(Object object, double seconds) {
        object.setPosition(object.getX(), seconds, object.getZ());
    }

    @Override
    public PropagationData setupObject(Object object, VisualizationObject initObject) {
        return null;
    }
}
