package com.lerstudios.space_debris_simulation.simulation.OrbitPopulations.OrbitDataSource;

import com.lerstudios.space_debris_simulation.simulation.types.SourceType;

public interface OrbitDataSource {
    String getName();
    void setName(String name);
    SourceType getType();
}
