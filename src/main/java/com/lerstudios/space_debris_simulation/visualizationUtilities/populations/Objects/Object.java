package com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects;

import com.lerstudios.space_debris_simulation.simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.VisualizationData.VisualizationData;

public interface Object {
    String getName();
    ObjectClassification getClassification();

    // Position getters
    double getX();
    double getY();
    double getZ();

    // Velocity getters
    double getVx();
    double getVy();
    double getVz();

    // Optional setters if you want mutability
    void setPosition(double x, double y, double z);
    void setVelocity(double vx, double vy, double vz);

    VisualizationData getVisualizationData();
    void setVisualizationData(VisualizationData data);

    PropagationData getPropagationData();
    void setPropagationData(PropagationData data);
}
