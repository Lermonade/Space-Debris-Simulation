package com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.removalMethods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lerstudios.space_debris_simulation.types.RemovalMethodType;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = OrbitalLaserRemovalMethod.class,
                name = "ORBITAL_LASER"
        )
})
public interface RemovalMethodTemplate {
    @JsonIgnore
    RemovalMethodType getRemovalMethodType();
}
