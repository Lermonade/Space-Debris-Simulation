package com.lerstudios.space_debris_simulation.modules.simulation.populations.dataSources;

import com.lerstudios.space_debris_simulation.types.SourceType;

public interface Simulation_OrbitDataSource {
    String getName();
    void setName(String name);
    SourceType getType();
}
