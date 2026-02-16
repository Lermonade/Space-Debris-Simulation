package com.lerstudios.space_debris_simulation.modules.visualization;

import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PointInterpolationPropagationMethod;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.SimToVis_File;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.SimToVis_Population;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.objects.SimToVis_ObjectBase;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.removalMethods.SimToVis_RemovalMethod;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.removalMethods.SimToVis_OrbitalLaser;
import com.lerstudios.space_debris_simulation.modules.visualization.SceneObjects.ObjectCam;
import com.lerstudios.space_debris_simulation.modules.visualization.populations.RemovalMethods.Visualization_RemovalMethod;
import com.lerstudios.space_debris_simulation.modules.visualization.populations.RemovalMethods.Visualization_OrbitalLaser;
import com.lerstudios.space_debris_simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.types.RemovalMethodType;
import com.lerstudios.space_debris_simulation.types.RenderingMethod;
import com.lerstudios.space_debris_simulation.utils.Constants;
import com.lerstudios.space_debris_simulation.modules.visualization.populations.Visualization_Population;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.DynamicSwitchingPropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.StaticKeplerianPropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.Object3DVisualizationMethod;
import com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.ParticleBillboardsVisualizationMethod;
import com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.VisualizationMethod;

import java.util.ArrayList;

public class VisualizationMain {
    SimToVis_File file;

    ArrayList<Visualization_Population> populations = new ArrayList<>();
    ArrayList<Visualization_RemovalMethod> removalMethods = new ArrayList<>();

    VisualizationGraphics graphics;

    public VisualizationMain(SimToVis_File file, VisualizationGraphics graphics) {
        this.file = file;
        this.graphics = graphics;
    }

    public void updateScene(double seconds) {
        for(Visualization_Population p: populations) {
            p.updateAllObjects(seconds);
        }
        for(Visualization_RemovalMethod p: removalMethods) {
            p.updateObject(seconds);
        }
        ParticleBillboardsVisualizationMethod.renderAllParticles();
        ObjectCam.updateAllCameras();
    }

    public void initializeVisualization() {
        Constants.setBodyRadius(file.celestialBodyRadius);
        Constants.setGravitationalConstant(file.gravitationalConstant);
        Constants.setEarthMass(file.celestialBodyMass);

        Timing.secondsPerTimeStep = file.timeStep;
        Timing.setMaxSeconds(file.timeStep * file.totalTimeSteps);

        setupPopulations(0);
        setupRemovalMethods(0);

    }

    private void setupPopulations(double seconds) {
        ArrayList<SimToVis_Population> initPopulations = file.populations;

        for(SimToVis_Population p: initPopulations) {
            PropagationMethod propagator = null;
            VisualizationMethod visualizer = null;

            if(p.renderingMethod == RenderingMethod.OBJECTS_3D) {
                visualizer = new Object3DVisualizationMethod(p.color, 0.5);
            } else if (p.renderingMethod == RenderingMethod.PARTICLE_BILLBOARDS) {
                visualizer = new ParticleBillboardsVisualizationMethod(p.color, graphics);
            }

            if(p.propegationMethod == PropegationMethod.STATIC_KEPLERIAN) {
                propagator = new StaticKeplerianPropagationMethod(false, true);
            } else if(p.propegationMethod == PropegationMethod.DYNAMIC_SWITCHING) {
                propagator = new PointInterpolationPropagationMethod();
            } else if(p.propegationMethod == PropegationMethod.SIMPLIFIED_VELOCITY_VERLET) {
                propagator = new PointInterpolationPropagationMethod();
            }

            Visualization_Population population = new Visualization_Population(p.populationName, propagator, visualizer);

            for(SimToVis_ObjectBase object : p.orbitalObjects) {
                population.addObject(object, graphics, seconds);
            }

            populations.add(population);
        }
    }

    private void setupRemovalMethods(double seconds) {
        for(SimToVis_RemovalMethod obj : file.removalMethods) {
            if(obj.getRemovalMethodType() == RemovalMethodType.ORBITAL_LASER) {
                removalMethods.add(
                        Visualization_OrbitalLaser.build((SimToVis_OrbitalLaser) obj, graphics, seconds)
                );
            }
        }
    }
}
