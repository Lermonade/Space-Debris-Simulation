package com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.VisualizationData;

public class VisualizationDataParticle implements VisualizationData {
    private int index;

    public VisualizationDataParticle(int index) {
        this.index = index;
    }

    @Override
    public java.lang.Object getVisualizationData() {
        return this.index; // returns as Object
    }
}
