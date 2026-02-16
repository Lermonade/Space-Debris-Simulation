package com.lerstudios.space_debris_simulation.interfaces.PropagationMethods;

import com.lerstudios.space_debris_simulation.interfaces.Objects.PropagationDataObject;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;

public interface PropagationMethod {
    void getPositionFromTime(PropagationDataObject object, double seconds);
    PropagationData setupObject(PropagationDataObject object, PropagationData data);
}
