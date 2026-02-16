package com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lerstudios.space_debris_simulation.interfaces.Objects.PropagationDataObject;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData.SimToVis_PropagationData;
import com.lerstudios.space_debris_simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.types.TimedCoordinates;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimToVis_StaticKeplerianObject.class, name = "VisualizationStaticKeplerianObject"),
        @JsonSubTypes.Type(value = SimToVis_DynamicSwitchingObject.class, name = "VisualizationDynamicSwitchingObject"),
        @JsonSubTypes.Type(value = SimToVis_SimplifiedVelocityVerletObject.class, name = "SimplifiedVelocityVerletObject")
})
public interface SimToVis_ObjectBase {
    String getName();
    ObjectClassification getClassification();
    PropegationMethod getPropagationMethod();
    SimToVis_PropagationData getPropagationData();
    TimedCoordinates getEndData();
}