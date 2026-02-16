package com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = SimToVis_StaticKeplerianPropagationData.class,
                name = "keplerian"
        ),
        @JsonSubTypes.Type(
                value = SimToVis_SimplifiedVelocityVerletPropagationData.class,
                name = "simplifiedvelocityverlet"
        ),
        @JsonSubTypes.Type(
                value = SimToVis_DynamicSwitchingPropagationData.class,
                name = "dynamicswitching"
        )
})
public interface SimToVis_PropagationData {
    @JsonIgnore
    Object getPropagationData();
}
