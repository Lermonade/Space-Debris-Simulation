package com.lerstudios.space_debris_simulation.modules.simulation.exportFormats;

import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.objects.SimToVis_ObjectBase;
import com.lerstudios.space_debris_simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.types.RenderingMethod;

import java.util.ArrayList;

public class SimToVis_Population {
    public String populationName;
    public ObjectClassification classification;
    public PropegationMethod propegationMethod;
    public RenderingMethod renderingMethod;
    public String color;

    public ArrayList<SimToVis_ObjectBase> orbitalObjects = new ArrayList<>();

    public SimToVis_Population() {
    }

    public SimToVis_Population(String populationName, ObjectClassification classification,
                               PropegationMethod propegationMethod, RenderingMethod renderingMethod,
                               String color, ArrayList<SimToVis_ObjectBase> orbitalObjects) {
        this.populationName = populationName;
        this.classification = classification;
        this.propegationMethod = propegationMethod;
        this.renderingMethod = renderingMethod;
        this.color = color;
        this.orbitalObjects = orbitalObjects;
    }
}
