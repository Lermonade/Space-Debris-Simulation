package com.lerstudios.space_debris_simulation.interfaces.Objects;

import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;

public interface PropagationDataObject extends Object3D {
    PropagationData getPropagationData();
    void setPropagationData(PropagationData data);
}
