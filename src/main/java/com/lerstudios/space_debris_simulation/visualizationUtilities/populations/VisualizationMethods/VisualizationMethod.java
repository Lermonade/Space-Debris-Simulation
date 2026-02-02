package com.lerstudios.space_debris_simulation.visualizationUtilities.populations.VisualizationMethods;

import com.lerstudios.space_debris_simulation.visualizationUtilities.VisualizationGraphics;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.Object;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.VisualizationData.VisualizationData;

public interface VisualizationMethod {
    void moveObject(Object object, double seconds);
    VisualizationData setupObject(Object object, VisualizationGraphics graphics);
}
