package com.lerstudios.space_debris_simulation.simulation.OrbitDataSource;

import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.orbitSources.SourceType;

public interface OrbitDataSource {
    String getName();
    void setName(String name);
    SourceType getType();
}
