package com.lerstudios.space_debris_simulation.simulation.OrbitPopulations;

import com.lerstudios.space_debris_simulation.simulation.Propagation.PopulationObjects.DynamicSwitchingObject;
import com.lerstudios.space_debris_simulation.simulation.types.SourceType;
import com.lerstudios.space_debris_simulation.simulation.OrbitPopulations.OrbitDataSource.KeplerianDistribution;
import com.lerstudios.space_debris_simulation.simulation.SimUtils;
import com.lerstudios.space_debris_simulation.simulation.OrbitPopulations.OrbitDataSource.OrbitDataSource;
import com.lerstudios.space_debris_simulation.simulation.Propagation.PopulationObjects.StaticKeplerianObject;
import com.lerstudios.space_debris_simulation.simulation.Propagation.PopulationObjects.OrbitalObject;
import com.lerstudios.space_debris_simulation.simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.visualizationUtilities.RenderingMethod;

import java.util.ArrayList;
import java.util.Map;

/*

A population of non-removal objects in the simulation.
This includes typically debris objects and satellites.

This class generates the populations at the start of the simulation based on a dataSource,
which provides spawning rules.

*/

public class OrbitalPopulation {
    public String populationName;
    public ObjectClassification classification;
    public PropegationMethod propegationMethod;
    public RenderingMethod renderingMethod;
    public String color;
    private int count;
    private OrbitDataSource source;

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

        this.generatePopulationObjects();
    }

    private void generatePopulationObjects() {
        for(int i = 0; i < this.count; i++) {
            int objectID = SimUtils.getNextID();

            if(this.source.getType() == SourceType.KEPLERIAN) {
                KeplerianDistribution kdSource = (KeplerianDistribution) this.source;
                OrbitalObject object = null;

                if(this.propegationMethod == PropegationMethod.STATIC_KEPLERIAN) {
                    object = new StaticKeplerianObject(this.populationName + ": " + objectID, kdSource.generateElements(), this.classification);
                } else if(this.propegationMethod == PropegationMethod.DYNAMIC_SWITCHING) {
                    object = new DynamicSwitchingObject(this.populationName + ": " + objectID, this.classification);
                }

                orbitalObjects.add(object);
            }
        }
    }
}
