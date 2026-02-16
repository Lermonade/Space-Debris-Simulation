package com.lerstudios.space_debris_simulation.modules.simulation;

import com.lerstudios.space_debris_simulation.interfaces.Objects.Object3D;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.Simulation_Population;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.populationObjects.Simulation_ObjectBase;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Results {
    double graphPoints;
    double[] pointTimes;

    boolean graphTotalDebrisPopulation = true;
    Map<Double, Double> totalDebrisPopulations = new TreeMap<>();

    boolean graphObjectsTargeted = true;
    Map<Double, Double> objectsTargeted = new TreeMap<>();

    public Results(int graphPoints, double totalTimeSteps) {
        this.graphPoints = graphPoints;
        pointTimes = new double[graphPoints];

        if (graphPoints < 2) {
            throw new IllegalArgumentException("graphPoints must be at least 2 to include start and end.");
        }

        for (int i = 0; i < graphPoints; i++) {
            // Compute evenly spaced points
            double t = i * (totalTimeSteps / (graphPoints - 1));
            pointTimes[i] = Math.round(t); // round to nearest long
        }

        // Ensure first and last points are exactly 0 and totalTimeSteps
        pointTimes[0] = 0;
        pointTimes[graphPoints - 1] = totalTimeSteps;
    }


    public void update(double timeStep) {
        boolean hit = false;

        double EPSILON = 1e-6;

        for (double pt : pointTimes) {
            if (Math.abs(pt - timeStep) < EPSILON) {
                hit = true;
                break;
            }
        }


        if (hit) {

            if(graphTotalDebrisPopulation) {

                double value = 0;
                for(Simulation_ObjectBase object: Simulation_Population.allDebrisObjects) {
                    if(!object.isPropagating() && object.getLastTargeter() != null) {
                        value++;
                    }
                }

                totalDebrisPopulations.put(timeStep, value);
            }

            if(graphObjectsTargeted) {

                double value = 0;
                for(Simulation_ObjectBase object: Simulation_Population.allDebrisObjects) {
                    if(object.getWasTargeted()) {
                        value++;
                    }
                }

                objectsTargeted.put(timeStep, value);
            }

        }
    }

    public void printFinalValues() {
        if(graphTotalDebrisPopulation) {
            System.out.println("Total Debris Removed");
            for (Map.Entry<Double, Double> entry : totalDebrisPopulations.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
        }

        if(graphTotalDebrisPopulation) {
            System.out.println("Total Objects Targeted");
            for (Map.Entry<Double, Double> entry : objectsTargeted.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
        }
    }

}
