package com.lerstudios.space_debris_simulation.visualizationUtilities.populations.PropagationMethods;

import com.lerstudios.space_debris_simulation.simulation.exportFormats.objects.VisualizationObject;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.Object;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.PropagationData.PropagationData;

public interface PropagationMethod {
    void getPositionFromTime(Object object, double seconds);
    PropagationData setupObject(Object object, VisualizationObject initObject);
}
