package com.lerstudios.space_debris_simulation.simulation.exportFormats;

import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.SimulationConstant;

import java.util.ArrayList;

public class VisualizationFile {
    public String simulationName;
    public ArrayList<VisualizationPopulation> populations;

    public int timeStep;
    public int totalTimeSteps;
    public double gravitationalConstant;
    public String celestialBodyName;
    public double celestialBodyRadius;
    public double celestialBodyMass;


    public VisualizationFile() {
        this.populations = new ArrayList<>();
    }

    public VisualizationFile(String simulationName, ArrayList<VisualizationPopulation> populations,
                             int timeStep, int totalTimeSteps, double gravitationalConstant,
                             String celestialBodyName, double celestialBodyRadius, double celestialBodyMass) {
        this.simulationName = simulationName;
        this.populations = populations;

        this.timeStep = timeStep;
        this.totalTimeSteps = totalTimeSteps;
        this.gravitationalConstant = gravitationalConstant;
        this.celestialBodyName = celestialBodyName;
        this.celestialBodyRadius = celestialBodyRadius;
        this.celestialBodyMass = celestialBodyMass;
    }
}
