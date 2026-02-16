package com.lerstudios.space_debris_simulation.modules.simulation;

import com.lerstudios.space_debris_simulation.Console;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.DynamicSwitchingPropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.DynamicSwitchingPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.SimplifiedVelocityVerletPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.StaticKeplerianPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.SimplifiedVelocityVerletPropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.StaticKeplerianPropagationMethod;
import com.lerstudios.space_debris_simulation.modules.configuration.SimulationSettings;
import com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.PopulationObject;
import com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.orbitSources.KeplerianDistributionSource;
import com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.orbitSources.SourceTemplate;
import com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.removalMethods.OrbitalLaserRemovalMethod;
import com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.removalMethods.RemovalMethodTemplate;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.Simulation_Population;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.dataSources.Simulation_KeplerianDistributionSource;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.dataSources.Simulation_OrbitDataSource;
import com.lerstudios.space_debris_simulation.modules.simulation.removalMethods.Simulation_OrbitalLaser;
import com.lerstudios.space_debris_simulation.modules.simulation.removalMethods.Simulation_RemovalMethod;
import com.lerstudios.space_debris_simulation.modules.simulation.removalMethods.Simulation_RemovalMethodBase;
import com.lerstudios.space_debris_simulation.types.*;
import com.lerstudios.space_debris_simulation.utils.Constants;
import com.lerstudios.space_debris_simulation.utils.PropagationTools;
import org.fxyz3d.geometry.Point3D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParseSettingsFile {
    static ArrayList<Simulation_Population> generateOrbitalPopulations(SimulationSettings settings, Console console) {
        Map<String, Simulation_OrbitDataSource> sourceMap = new HashMap<>();


        ArrayList<Simulation_Population> populations = new ArrayList<>();

        for (SourceTemplate source : settings.sources) {
            if (source.getSourceType() == SourceType.KEPLERIAN) {
                sourceMap.put(source.getName(), new Simulation_KeplerianDistributionSource(source.getName(), (KeplerianDistributionSource) source));
            }
        }

        for (PopulationObject population : settings.populationObjects) {

            String populationName = population.populationName;
            ObjectClassification classification = ObjectClassification.DEBRIS;
            PropegationMethod propegationMethod = PropegationMethod.DYNAMIC_SWITCHING;
            RenderingMethod renderingMethod = RenderingMethod.OBJECTS_3D;
            String color = population.renderingColor;
            String sourceName = population.source;
            int count2 = Integer.parseInt(population.count);

            if (population.objectClassification.equals("Operational Satellite")) {
                classification = ObjectClassification.SATELLITE;
            } else if (population.objectClassification.equals("Debris")) {
                classification = ObjectClassification.DEBRIS;
            }

            if (population.propagationMethod.equals("Two-Body Keplerian")) {
                propegationMethod = PropegationMethod.STATIC_KEPLERIAN;
            } else if (population.propagationMethod.equals("Dynamic Propegation Switching")) {
                propegationMethod = PropegationMethod.DYNAMIC_SWITCHING;
            } else if (population.propagationMethod.equals("Simplified Velocity Verlet N-Body")) {
                propegationMethod = PropegationMethod.SIMPLIFIED_VELOCITY_VERLET;
            }

            if (population.renderingMethod.equals("3D Objects")) {
                renderingMethod = RenderingMethod.OBJECTS_3D;
            } else if (population.renderingMethod.equals("Particle Billboards")) {
                renderingMethod = RenderingMethod.PARTICLE_BILLBOARDS;
            }

            Simulation_Population population1 = new Simulation_Population(
                    populationName,
                    classification,
                    propegationMethod,
                    renderingMethod,
                    color,
                    sourceName,
                    count2,
                    sourceMap
            );

            populations.add(population1);
        }

        console.log("Created " + SimUtils.getCurrentID() + " simulation objects!");
        return populations;
    }

    static ArrayList<Simulation_RemovalMethodBase> generateRemovalMethods(SimulationSettings settings) {
        ArrayList<Simulation_RemovalMethodBase> newstuff = new ArrayList<>();

        for(RemovalMethodTemplate t : settings.removalmethods) {
            if(t.getRemovalMethodType() == RemovalMethodType.ORBITAL_LASER) {
                newstuff.add(parseOrbitalLaser((OrbitalLaserRemovalMethod) t));
            }
        }

        return newstuff;
    }

    public static Simulation_OrbitalLaser parseOrbitalLaser(OrbitalLaserRemovalMethod method) {
        if (method == null) return null;

        PropegationMethod p = null;
        PropagationMethod propagator = null;
        PropagationData data = null;

        if (method.propagationMethod.equals("Two-Body Keplerian")) {
            p = PropegationMethod.STATIC_KEPLERIAN;
            propagator = new StaticKeplerianPropagationMethod(true, false);
            data = new StaticKeplerianPropagationData(RadianKeplerianElements.fromDegrees(method.orbitalData));
        } else if (method.propagationMethod.equals("Dynamic Propegation Switching")) {
            p = PropegationMethod.DYNAMIC_SWITCHING;
            propagator = new DynamicSwitchingPropagationMethod();
            data = new DynamicSwitchingPropagationData(RadianKeplerianElements.fromDegrees(method.orbitalData));
        } else if(method.propagationMethod.equals("Simplified Velocity Verlet N-Body")) {
            p = PropegationMethod.SIMPLIFIED_VELOCITY_VERLET;
            propagator = new SimplifiedVelocityVerletPropagationMethod();
            // Data Below
        }

        Simulation_OrbitalLaser output = new Simulation_OrbitalLaser(
                method.name,

                method.laserPowerWatts,
                method.wavelengthNanometers,
                method.apertureDiameterMeters,
                method.beamQualityM2,
                method.opticalEfficiencyPercent,

                method.pulseRateHz,
                method.pulseDurationPicoseconds,

                method.maxEngagementTimeSeconds,
                method.cooldownTimeSeconds,
                method.slewRateDegPerSec,
                method.targetingRangeMeters,

                method.collisionRadiusMeters,

                method.color,
                method.laserColor,
                method.drawLines,
                p,
                propagator
        );

        if(method.propagationMethod.equals("Simplified Velocity Verlet N-Body")) {
            assert propagator != null;

            RadianKeplerianElements initElements = RadianKeplerianElements.fromDegrees(method.orbitalData);

            Point3D initPos = PropagationTools.propagateOrbit(0, 0,0, initElements.semiMajorAxis(), initElements.eccentricity(), initElements.argPeriapsis(), initElements.inclination(), initElements.raan(), initElements.anomaly(), 0, Constants.gravitationalConstant * Constants.earthMass, true);
            output.setPosition(initPos.getX(), initPos.getY(), initPos.getZ());

            javafx.geometry.Point3D initVel = PropagationTools.propagateOrbitVelocity(0, 0,0, initElements.semiMajorAxis(), initElements.eccentricity(), initElements.argPeriapsis(), initElements.inclination(), initElements.raan(), initElements.anomaly(), 0, Constants.gravitationalConstant * Constants.earthMass, true);
            output.setVelocity(initVel.getX(), initVel.getY(), initVel.getZ());

            data = new SimplifiedVelocityVerletPropagationData();
            data = propagator.setupObject(output, data);
        }

        output.setPropagationData(data);

        return output;
    }
}
