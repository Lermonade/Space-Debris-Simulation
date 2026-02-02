package com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.PropagationData;

import com.lerstudios.space_debris_simulation.simulation.types.KeplerianElements;
import javafx.scene.Node;

public class StaticKeplerianPropagationData implements PropagationData {
    KeplerianElements elements;

    public StaticKeplerianPropagationData(KeplerianElements elements) {
        this.elements = elements;
    }

    @Override
    public Object getPropagationData() {
        return elements;
    }
}
