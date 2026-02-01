package com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.orbitSources;

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
                value = KeplerianDistributionSource.class,
                name = "KEPLERIAN"
        )
})
public interface SourceTemplate {
    SourceType getSourceType();
    String getName();
}
