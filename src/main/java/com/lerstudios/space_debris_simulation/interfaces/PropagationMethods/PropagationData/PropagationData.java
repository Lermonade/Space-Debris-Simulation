package com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StaticKeplerianPropagationData.class, name = "keplerian")
})
public interface PropagationData {
    Object getPropagationData();
}
