package com.lerstudios.space_debris_simulation.simulation.exportFormats;

import com.lerstudios.space_debris_simulation.simulation.OrbitPopulations.OrbitDataSource.OrbitDataSource;
import com.lerstudios.space_debris_simulation.simulation.Propagation.PopulationObjects.OrbitalObject;
import com.lerstudios.space_debris_simulation.simulation.exportFormats.objects.VisualizationObject;
import com.lerstudios.space_debris_simulation.simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.visualizationUtilities.RenderingMethod;

import java.util.ArrayList;

public class VisualizationPopulation {
    public String populationName;
    public ObjectClassification classification;
    public PropegationMethod propegationMethod;
    public RenderingMethod renderingMethod;
    public String color;

    public ArrayList<VisualizationObject> orbitalObjects = new ArrayList<>();

    public VisualizationPopulation() {
    }

    public VisualizationPopulation(String populationName, ObjectClassification classification,
                             PropegationMethod propegationMethod, RenderingMethod renderingMethod,
                             String color, ArrayList<VisualizationObject> orbitalObjects) {
        this.populationName = populationName;
        this.classification = classification;
        this.propegationMethod = propegationMethod;
        this.renderingMethod = renderingMethod;
        this.color = color;
        this.orbitalObjects = orbitalObjects;
    }
}
