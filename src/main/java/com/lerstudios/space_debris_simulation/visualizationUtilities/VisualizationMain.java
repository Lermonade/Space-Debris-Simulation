package com.lerstudios.space_debris_simulation.visualizationUtilities;

import com.lerstudios.space_debris_simulation.simulation.exportFormats.VisualizationFile;
import com.lerstudios.space_debris_simulation.simulation.exportFormats.VisualizationPopulation;
import com.lerstudios.space_debris_simulation.simulation.exportFormats.objects.VisualizationObject;
import com.lerstudios.space_debris_simulation.simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.utils.Constants;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Population;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.PropagationMethods.DynamicSwitchingPropagationMethod;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.PropagationMethods.StaticKeplerianPropagationMethod;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.VisualizationMethods.Object3DVisualizationMethod;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.VisualizationMethods.VisualizationMethod;

import java.io.File;
import java.util.ArrayList;

public class VisualizationMain {
    VisualizationFile file;
    ArrayList<Population> populations = new ArrayList<>();
    VisualizationGraphics graphics;

    public VisualizationMain(VisualizationFile file, VisualizationGraphics graphics) {
        this.file = file;
        this.graphics = graphics;
    }

    public void updateScene(double seconds) {
        for(Population p: populations) {
            p.updateAllObjects(seconds);
        }
    }

    public void initializeVisualization() {
        Constants.setScaleFactor(file.celestialBodyRadius);
        Constants.setGravitationalConstant(file.gravitationalConstant);
        Constants.setEarthMass(file.celestialBodyMass);

        Timing.setMaxSeconds(file.timeStep * file.totalTimeSteps);

        setupPopulations(0);

    }

    private void setupPopulations(double seconds) {
        ArrayList<VisualizationPopulation> initPopulations = file.populations;

        for(VisualizationPopulation p: initPopulations) {
            PropagationMethod propagator = null;
            VisualizationMethod visualizer = null;

            if(p.renderingMethod == RenderingMethod.OBJECTS_3D) {
                visualizer = new Object3DVisualizationMethod(p.color);
            }

            if(p.propegationMethod == PropegationMethod.STATIC_KEPLERIAN) {
                propagator = new StaticKeplerianPropagationMethod();
            } else if(p.propegationMethod == PropegationMethod.DYNAMIC_SWITCHING) {
                propagator = new DynamicSwitchingPropagationMethod();
            }

            Population population = new Population(p.populationName, propagator, visualizer);

            for(VisualizationObject object : p.orbitalObjects) {
                population.addObject(object, graphics, seconds);
            }

            populations.add(population);
        }
    }
}
