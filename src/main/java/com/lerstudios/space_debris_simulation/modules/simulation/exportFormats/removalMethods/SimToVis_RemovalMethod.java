package com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.removalMethods;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lerstudios.space_debris_simulation.types.RemovalMethodType;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimToVis_OrbitalLaser.class, name = "VisualizationOrbitalDebrisRemovalLaser"),
})
public interface SimToVis_RemovalMethod {
    RemovalMethodType getRemovalMethodType();
}
