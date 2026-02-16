package com.lerstudios.space_debris_simulation.modules.simulation.populations;

import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.DynamicSwitchingPropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.SimplifiedVelocityVerletPropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.StaticKeplerianPropagationMethod;
import com.lerstudios.space_debris_simulation.modules.simulation.Collisions;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.populationObjects.*;
import com.lerstudios.space_debris_simulation.types.*;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.dataSources.Simulation_KeplerianDistributionSource;
import com.lerstudios.space_debris_simulation.modules.simulation.SimUtils;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.dataSources.Simulation_OrbitDataSource;

import java.util.ArrayList;
import java.util.Map;

/*

A population of non-removal objects in the simulation.
This includes typically debris objects and satellites.

This class generates the populations at the start of the simulation based on a dataSource,
which provides spawning rules.

*/

public class Simulation_Population {
    public String populationName;
    public ObjectClassification classification;
    public PropegationMethod propegationMethod;
    public RenderingMethod renderingMethod;
    public String color;
    private int count;
    private Simulation_OrbitDataSource source;

    public PropagationMethod propagator;

    public ArrayList<Simulation_ObjectBase> orbitalObjects = new ArrayList<>();

    public static ArrayList<Simulation_ObjectBase> allDebrisObjects = new ArrayList<>();

    public Simulation_Population(String populationName, ObjectClassification classification,
                                 PropegationMethod propegationMethod, RenderingMethod renderingMethod,
                                 String color, String sourceName, int count, Map<String, Simulation_OrbitDataSource> sourceMap) {
        this.populationName = populationName;
        this.classification = classification;
        this.propegationMethod = propegationMethod;
        this.renderingMethod = renderingMethod;
        this.color = color;
        this.count = count;

        if(this.propegationMethod == PropegationMethod.STATIC_KEPLERIAN) {
            propagator = new StaticKeplerianPropagationMethod(true, false);
        } else if(this.propegationMethod == PropegationMethod.SIMPLIFIED_VELOCITY_VERLET) {
            propagator = new SimplifiedVelocityVerletPropagationMethod();
        } else if(this.propegationMethod == PropegationMethod.DYNAMIC_SWITCHING) {
            propagator = new DynamicSwitchingPropagationMethod();
        }

        this.source = sourceMap.getOrDefault(sourceName, null);

        this.generatePopulationObjects();
    }

    public void updateAllObjects(double timestep) {
        orbitalObjects
                .parallelStream()
                .forEach(object -> object.propagate(timestep, this.propagator));

    }

    private void generatePopulationObjects() {
        for(int i = 0; i < this.count; i++) {
            int objectID = SimUtils.getNextID();

            if(this.source.getType() == SourceType.KEPLERIAN) {
                Simulation_KeplerianDistributionSource kdSource = (Simulation_KeplerianDistributionSource) this.source;
                Simulation_ObjectBase object = null;

                if(this.propegationMethod == PropegationMethod.STATIC_KEPLERIAN) {
                    RadianKeplerianElements elements = RadianKeplerianElements.fromDegrees(kdSource.generateElements());
                    object = new Simulation_StaticKeplerianObject(this.populationName + ": " + objectID, elements, this.classification);
                    object.setMass(elements.mass());
                } else if(this.propegationMethod == PropegationMethod.DYNAMIC_SWITCHING) {
                    RadianKeplerianElements elements = RadianKeplerianElements.fromDegrees(kdSource.generateElements());
                    object = new Simulation_DynamicSwitchingObject(this.populationName + ": " + objectID, this.classification, elements);
                    object.setMass(elements.mass());
                } else if(this.propegationMethod == PropegationMethod.SIMPLIFIED_VELOCITY_VERLET) {
                    RadianKeplerianElements elements = RadianKeplerianElements.fromDegrees(kdSource.generateElements());
                    object = new Simulation_SimplifiedVelocityVerletObject(this.populationName + ": " + objectID, this.classification, propagator, elements);
                    object.setMass(elements.mass());
                }

                orbitalObjects.add(object);

                if(object.getClassification() == ObjectClassification.DEBRIS) {
                    Simulation_Population.allDebrisObjects.add(object);
                }
            }
        }
    }
}
