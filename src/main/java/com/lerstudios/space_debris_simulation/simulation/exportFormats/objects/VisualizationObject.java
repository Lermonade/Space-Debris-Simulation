package com.lerstudios.space_debris_simulation.simulation.exportFormats.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lerstudios.space_debris_simulation.simulation.Propagation.PopulationObjects.DynamicSwitchingObject;
import com.lerstudios.space_debris_simulation.simulation.Propagation.PopulationObjects.StaticKeplerianObject;
import com.lerstudios.space_debris_simulation.simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.simulation.types.PropegationMethod;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = VisualizationStaticKeplerianObject.class, name = "VisualizationStaticKeplerianObject"),
        @JsonSubTypes.Type(value = VisualizationDynamicSwitchingObject.class, name = "VisualizationDynamicSwitchingObject")
})
public interface VisualizationObject {
    String getName();
    PropegationMethod getPropagationMethod();
    ObjectClassification getClassification();

    @JsonIgnore
    Object getOrbitData();
}