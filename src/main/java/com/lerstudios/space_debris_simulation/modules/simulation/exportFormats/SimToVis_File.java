package com.lerstudios.space_debris_simulation.modules.simulation.exportFormats;

import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.removalMethods.SimToVis_RemovalMethod;

import java.util.ArrayList;

public class SimToVis_File {
    public String simulationName;
    public ArrayList<SimToVis_Population> populations;
    public ArrayList<SimToVis_RemovalMethod> removalMethods;

    public int timeStep;
    public int totalTimeSteps;
    public double gravitationalConstant;
    public String celestialBodyName;
    public double celestialBodyRadius;
    public double celestialBodyMass;


    public SimToVis_File() {
        this.populations = new ArrayList<>();
    }

    public SimToVis_File(String simulationName, ArrayList<SimToVis_Population> populations, ArrayList<SimToVis_RemovalMethod> removalMethods,
                         int timeStep, int totalTimeSteps, double gravitationalConstant,
                         String celestialBodyName, double celestialBodyRadius, double celestialBodyMass) {
        this.simulationName = simulationName;
        this.populations = populations;
        this.removalMethods = removalMethods;

        this.timeStep = timeStep;
        this.totalTimeSteps = totalTimeSteps;
        this.gravitationalConstant = gravitationalConstant;
        this.celestialBodyName = celestialBodyName;
        this.celestialBodyRadius = celestialBodyRadius;
        this.celestialBodyMass = celestialBodyMass;
    }
}
