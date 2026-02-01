package com.lerstudios.space_debris_simulation.simulation.Propagation;

import com.lerstudios.space_debris_simulation.simulation.SimUtils;

import java.util.ArrayList;

public class KeplerianOrbitalObject implements OrbitalObject {
    String name;
    public ArrayList<TimedKeplerianElements> elements = new ArrayList<>();
    boolean propagating = true;

    public KeplerianOrbitalObject(String name) {
        this.name = name;
    }

    public void addKeplerian(KeplerianElements elements) {
        // adds a keplerian element set to the array, starting at this time step and ending at the end time step
        TimedKeplerianElements newElements = new TimedKeplerianElements(elements, SimUtils.getCurrentTimeStep(), SimUtils.getEndTimeStep());
        this.elements.add(newElements);
    }

    public void stopKeplerian(double timeStep) {
        // called when the sim switches to n-body physics.
        // sets the last element in the list's ending position to the current time step
    }

    public void stopPropagating(double timeStep, KeplerianElements elements) {
        // called when propegation should stop.
        // Adds a last keplerianElements at the end to ensure a smooth lerp. This will end and start at the listed timestep.

        // if the last element's time step is equal to the final time step, run stopKeplerian
        // else: addkeplerian stopkeplerian

        propagating = false;
    }


    @Override
    public String getName() {
        return this.name;
    }
}
