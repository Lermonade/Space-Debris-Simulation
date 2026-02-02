package com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.VisualizationData;

import javafx.scene.Node;

public class VisualizationDataNode implements VisualizationData {
    private final Node visualizationNode;

    public VisualizationDataNode(Node node) {
        this.visualizationNode = node;
    }

    @Override
    public java.lang.Object getVisualizationData() {
        return this.visualizationNode; // returns as Object
    }
}

