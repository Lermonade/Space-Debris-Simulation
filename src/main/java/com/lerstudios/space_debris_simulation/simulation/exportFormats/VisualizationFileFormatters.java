package com.lerstudios.space_debris_simulation.simulation.exportFormats;

import com.lerstudios.space_debris_simulation.configurationUtilities.SimulationSettings;
import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.SimulationConstant;
import com.lerstudios.space_debris_simulation.simulation.OrbitPopulations.OrbitalPopulation;
import com.lerstudios.space_debris_simulation.simulation.Propagation.PopulationObjects.DynamicSwitchingObject;
import com.lerstudios.space_debris_simulation.simulation.Propagation.PopulationObjects.OrbitalObject;
import com.lerstudios.space_debris_simulation.simulation.Propagation.PopulationObjects.StaticKeplerianObject;
import com.lerstudios.space_debris_simulation.simulation.Simulation;
import com.lerstudios.space_debris_simulation.simulation.exportFormats.objects.VisualizationDynamicSwitchingObject;
import com.lerstudios.space_debris_simulation.simulation.exportFormats.objects.VisualizationObject;
import com.lerstudios.space_debris_simulation.simulation.exportFormats.objects.VisualizationStaticKeplerianObject;
import com.lerstudios.space_debris_simulation.simulation.types.PropegationMethod;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class VisualizationFileFormatters {
    public static VisualizationFile createVisualizationFile(SimulationSettings settings, ArrayList<OrbitalPopulation> populations) {
        ArrayList<VisualizationPopulation> newPopulations = new ArrayList<>();

        for(OrbitalPopulation population : populations) {
            newPopulations.add(createVisualizationPopulation(population));
        }

        int timeStep =
                ((Number) settings.getOrDefault("Time Step (s)", 10)).intValue();

        int timeSteps =
                ((Number) settings.getOrDefault("Total Time Steps", 5000)).intValue();

        double gConst =
                ((Number) settings.getOrDefault("Gravitational Constant", 6.67430e-11)).doubleValue();

        String cName =
                settings.getOrDefault("Celestial Body Name", "Earth").toString();

        double cRadius =
                ((Number) settings.getOrDefault("Celestial Body Radius", 6371000)).doubleValue();

        double cMass =
                ((Number) settings.getOrDefault("Celestial Body Mass", 5.972e24)).doubleValue();

        VisualizationFile file = new VisualizationFile(settings.simulationName, newPopulations,
                timeStep, timeSteps, gConst, cName, cRadius, cMass);

        return file;
    }

    private static VisualizationPopulation createVisualizationPopulation(OrbitalPopulation population1) {
        ArrayList<OrbitalObject> objects = population1.orbitalObjects;
        ArrayList<VisualizationObject> newObjects = new ArrayList<>();

        for(OrbitalObject object : objects) {
            if(object.getPropagationMethod() == PropegationMethod.STATIC_KEPLERIAN) {
                newObjects.add(createVisualizationStaticKeplerianObject((StaticKeplerianObject) object));
            } else if(object.getPropagationMethod() == PropegationMethod.DYNAMIC_SWITCHING) {
                newObjects.add(createVisualizationDynamicSwitchingObject((DynamicSwitchingObject) object));
            }
        }

        return new VisualizationPopulation(population1.populationName,
                population1.classification,
                population1.propegationMethod,
                population1.renderingMethod,
                population1.color,
                newObjects);
    }

    private static VisualizationStaticKeplerianObject createVisualizationStaticKeplerianObject(StaticKeplerianObject o) {
        return new VisualizationStaticKeplerianObject(o.getName(), o.elements, o.getClassification());
    }

    private static VisualizationDynamicSwitchingObject createVisualizationDynamicSwitchingObject(DynamicSwitchingObject o) {
        return new VisualizationDynamicSwitchingObject(o.getName(), o.getClassification());
    }
}
