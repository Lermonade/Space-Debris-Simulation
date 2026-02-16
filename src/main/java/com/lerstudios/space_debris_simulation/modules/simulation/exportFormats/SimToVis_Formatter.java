package com.lerstudios.space_debris_simulation.modules.simulation.exportFormats;

import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.DynamicSwitchingPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.SimplifiedVelocityVerletPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.StaticKeplerianPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.modules.configuration.SimulationSettings;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.objects.SimToVis_SimplifiedVelocityVerletObject;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData.SimToVis_DynamicSwitchingPropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData.SimToVis_PropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData.SimToVis_SimplifiedVelocityVerletPropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData.SimToVis_StaticKeplerianPropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.Simulation_Population;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.populationObjects.*;
import com.lerstudios.space_debris_simulation.modules.simulation.removalMethods.Simulation_OrbitalLaser;
import com.lerstudios.space_debris_simulation.modules.simulation.removalMethods.Simulation_RemovalMethod;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.objects.SimToVis_DynamicSwitchingObject;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.objects.SimToVis_ObjectBase;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.objects.SimToVis_StaticKeplerianObject;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.removalMethods.SimToVis_RemovalMethod;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.removalMethods.SimToVis_OrbitalLaser;
import com.lerstudios.space_debris_simulation.modules.simulation.removalMethods.Simulation_RemovalMethodBase;
import com.lerstudios.space_debris_simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;
import com.lerstudios.space_debris_simulation.types.RemovalMethodType;
import com.lerstudios.space_debris_simulation.utils.Vector3;

import java.util.ArrayList;
import java.util.Map;

public class SimToVis_Formatter {
    public static SimToVis_File createVisualizationFile(SimulationSettings settings, ArrayList<Simulation_Population> populations, ArrayList<Simulation_RemovalMethodBase> removalMethods) {
        ArrayList<SimToVis_Population> newPopulations = new ArrayList<>();
        ArrayList<SimToVis_RemovalMethod> newRemovalMethods = new ArrayList<>();

        for(Simulation_Population population : populations) {
            newPopulations.add(createVisualizationPopulation(population));
        }

        for(Simulation_RemovalMethod r : removalMethods) {
            if(r.getRemovalMethodType() == RemovalMethodType.ORBITAL_LASER) {
                Simulation_OrbitalLaser m = (Simulation_OrbitalLaser) r;
                newRemovalMethods.add(SimToVis_Formatter.createVisualizationOrbitalDebrisRemovalLaserObject(m));
            }
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

        SimToVis_File file = new SimToVis_File(settings.simulationName, newPopulations, newRemovalMethods,
                timeStep, timeSteps, gConst, cName, cRadius, cMass);

        return file;
    }

    private static SimToVis_Population createVisualizationPopulation(Simulation_Population population1) {
        ArrayList<Simulation_ObjectBase> objects = population1.orbitalObjects;
        ArrayList<SimToVis_ObjectBase> newObjects = new ArrayList<>();

        for(Simulation_Object object : objects) {
            if(object.getPropagationMethod() == PropegationMethod.STATIC_KEPLERIAN) {
                newObjects.add(createVisualizationStaticKeplerianObject((Simulation_StaticKeplerianObject) object));
            } else if(object.getPropagationMethod() == PropegationMethod.DYNAMIC_SWITCHING) {
                newObjects.add(createVisualizationDynamicSwitchingObject((Simulation_DynamicSwitchingObject) object));
            } else if(object.getPropagationMethod() == PropegationMethod.SIMPLIFIED_VELOCITY_VERLET) {
                newObjects.add(createVisualizationSimplifiedVelocityVerletObject((Simulation_SimplifiedVelocityVerletObject) object));
            }
        }

        return new SimToVis_Population(population1.populationName,
                population1.classification,
                population1.propegationMethod,
                population1.renderingMethod,
                population1.color,
                newObjects);
    }

    private static SimToVis_StaticKeplerianObject createVisualizationStaticKeplerianObject(Simulation_StaticKeplerianObject o) {
        SimToVis_StaticKeplerianObject object = new SimToVis_StaticKeplerianObject(o.getName(), buildPropagationData(o), o.getClassification());
        if(o.propagationEndData != null) {
            object.endData = o.propagationEndData;
        }
        return object;
    }

    private static SimToVis_DynamicSwitchingObject createVisualizationDynamicSwitchingObject(Simulation_DynamicSwitchingObject o) {
        SimToVis_DynamicSwitchingObject object = new SimToVis_DynamicSwitchingObject(o.getName(), o.getClassification(), (SimToVis_DynamicSwitchingPropagationData) buildPropagationData(o));
        if(o.propagationEndData != null) {
            object.endData = o.propagationEndData;
        }
        return object;
    }

    private static SimToVis_SimplifiedVelocityVerletObject createVisualizationSimplifiedVelocityVerletObject(Simulation_SimplifiedVelocityVerletObject o) {
        SimToVis_SimplifiedVelocityVerletObject object = new SimToVis_SimplifiedVelocityVerletObject(o.getName(), buildPropagationData(o), o.getClassification());
        if(o.propagationEndData != null) {
            object.endData = o.propagationEndData;
        }
        return object;
    }

    public static SimToVis_OrbitalLaser createVisualizationOrbitalDebrisRemovalLaserObject(Simulation_OrbitalLaser method) {
        if (method == null) return null;

        SimToVis_OrbitalLaser object = new SimToVis_OrbitalLaser(
                method.getName(),
                method.color,
                method.laserColor,
                Boolean.parseBoolean(method.drawLines),
                method.getPropagationMethod(),
                buildPropagationData(method),
                method.getLaserTargetData());

        if(method.propagationEndData != null) {
            object.endData = method.propagationEndData;
        }

        return object;
    }

    public static SimToVis_PropagationData buildPropagationData(Simulation_ObjectBase method) {
        if(method.getPropagationData() == null) {
            System.out.println("PropagationData is null");
        }
        PropagationData init = method.getPropagationData();

        if(method.getPropagationMethod() == PropegationMethod.STATIC_KEPLERIAN) {
            StaticKeplerianPropagationData init2 = (StaticKeplerianPropagationData) init;
            return new SimToVis_StaticKeplerianPropagationData((RadianKeplerianElements) init2.getPropagationData());
        } else if (method.getPropagationMethod() == PropegationMethod.SIMPLIFIED_VELOCITY_VERLET) {
            SimplifiedVelocityVerletPropagationData data = (SimplifiedVelocityVerletPropagationData) init; // if it's typed
            Object bundleObj = data.getPropagationData();  // returns Object
            SimplifiedVelocityVerletPropagationData.AccelerationBundle bundle =
                    (SimplifiedVelocityVerletPropagationData.AccelerationBundle) bundleObj;

            return new SimToVis_SimplifiedVelocityVerletPropagationData(bundle.points);
        } else if (method.getPropagationMethod() == PropegationMethod.DYNAMIC_SWITCHING) {
            DynamicSwitchingPropagationData data = (DynamicSwitchingPropagationData) init; // if it's typed
            Map<Double, Vector3> points = data.getVisualizationPositions();
            return new SimToVis_DynamicSwitchingPropagationData(points);
        }

        return null;
    }

}
