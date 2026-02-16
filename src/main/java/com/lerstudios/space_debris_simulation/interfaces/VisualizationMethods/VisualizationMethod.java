package com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods;

import com.lerstudios.space_debris_simulation.interfaces.Objects.VisualizationDataObject;
import com.lerstudios.space_debris_simulation.modules.visualization.VisualizationGraphics;
import com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.VisualizationData.VisualizationData;

public interface VisualizationMethod {
    void moveObject(VisualizationDataObject object, double seconds);
    VisualizationData setupObject(VisualizationDataObject object, VisualizationGraphics graphics);
}
