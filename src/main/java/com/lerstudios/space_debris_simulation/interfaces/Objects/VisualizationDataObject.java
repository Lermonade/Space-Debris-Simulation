package com.lerstudios.space_debris_simulation.interfaces.Objects;

import com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.VisualizationData.VisualizationData;

public interface VisualizationDataObject extends Object3D {
    VisualizationData getVisualizationData();
    void setVisualizationData(VisualizationData data);
}
