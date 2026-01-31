package com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes;


public class PopulationObject {
    public String populationName;
    public String objectClassification;
    public String propagationMethod;
    public String renderingMethod;
    public String renderingColor;
    public String source;

    public PopulationObject() {

    }

    public PopulationObject(
            String populationName,
            String objectClassification,
            String propagationMethod,
            String renderingMethod,
            String renderingColor,
            String source
    ) {
        this.populationName = populationName;
        this.objectClassification = objectClassification;
        this.propagationMethod = propagationMethod;
        this.renderingMethod = renderingMethod;
        this.renderingColor = renderingColor;
        this.source = source;
    }
}
