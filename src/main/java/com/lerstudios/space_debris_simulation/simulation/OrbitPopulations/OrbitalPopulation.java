package com.lerstudios.space_debris_simulation.simulation.OrbitPopulations;

import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.orbitSources.SourceType;
import com.lerstudios.space_debris_simulation.simulation.OrbitDataSource.KeplerianDistribution;
import com.lerstudios.space_debris_simulation.simulation.SimUtils;
import com.lerstudios.space_debris_simulation.simulation.OrbitDataSource.OrbitDataSource;
import com.lerstudios.space_debris_simulation.simulation.Propagation.KeplerianOrbitalObject;
import com.lerstudios.space_debris_simulation.simulation.Propagation.OrbitalObject;
import com.lerstudios.space_debris_simulation.simulation.Propagation.PropegationMethod;
import com.lerstudios.space_debris_simulation.visualizationUtilities.RenderingMethod;

import java.util.ArrayList;
import java.util.Map;

public class OrbitalPopulation {
    String populationName;
    ObjectClassification classification;
    public PropegationMethod propegationMethod;
    RenderingMethod renderingMethod;
    String color;
    int count;

    OrbitDataSource source;

    public ArrayList<OrbitalObject> orbitalObjects = new ArrayList<>();

    public OrbitalPopulation(String populationName, ObjectClassification classification,
                             PropegationMethod propegationMethod, RenderingMethod renderingMethod,
                             String color, String sourceName, int count, Map<String, OrbitDataSource> sourceMap) {
        this.populationName = populationName;
        this.classification = classification;
        this.propegationMethod = propegationMethod;
        this.renderingMethod = renderingMethod;
        this.color = color;
        this.count = count;

        this.source = sourceMap.getOrDefault(sourceName, null);

        this.generateDebrisObjects();
    }

    public void generateDebrisObjects() {
        for(int i = 0; i < this.count; i++) {
            int objectID = SimUtils.getNextID();

            if(this.source.getType() == SourceType.KEPLERIAN) {
                KeplerianOrbitalObject object = new KeplerianOrbitalObject(this.populationName + ": " + objectID);
                KeplerianDistribution kdSource = (KeplerianDistribution) this.source;
                object.addKeplerian(kdSource.generateElements());

                orbitalObjects.add(object);
            }
        }
    }
}
