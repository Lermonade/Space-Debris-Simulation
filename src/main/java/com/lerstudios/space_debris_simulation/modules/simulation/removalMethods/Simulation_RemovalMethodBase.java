package com.lerstudios.space_debris_simulation.modules.simulation.removalMethods;

import com.lerstudios.space_debris_simulation.interfaces.Objects.propagatableObjects.StaticKeplerianPropagationObject;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.StaticKeplerianPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.modules.simulation.Collisions;
import com.lerstudios.space_debris_simulation.modules.simulation.SimUtils;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.populationObjects.Simulation_ObjectBase;
import com.lerstudios.space_debris_simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.types.RemovalMethodType;
import com.lerstudios.space_debris_simulation.types.TimedCoordinates;
import com.lerstudios.space_debris_simulation.utils.Constants;

public class Simulation_RemovalMethodBase extends Simulation_ObjectBase implements Simulation_RemovalMethod {
    public String color;
    public String drawLines;
    public PropagationMethod propagator;
    public RemovalMethodType removalMethodType;
    public boolean propagating = true;

    public Simulation_RemovalMethodBase(String name, ObjectClassification classification, PropegationMethod method, PropagationMethod propagator, String color, String drawLines, RemovalMethodType removalMethodType) {
        super(name, classification, method);
        this.propagator = propagator;
        this.color = color;
        this.drawLines = drawLines;
        this.removalMethodType = removalMethodType;
    }

    @Override
    public RemovalMethodType getRemovalMethodType() {
        return removalMethodType;
    }

    public void propagate(double timestep) {
        if(propagating) {
            propagator.getPositionFromTime(this, timestep * SimUtils.getSecondsPerTimeStep());

            if (Collisions.checkForPlanetaryCollision(this)) {
                Constants.console.log("[" + timestep + "] " + this.getName() + " collided with the planet!");
                this.propagating = false;

                this.propagationEndData = TimedCoordinates.build(this, (int) timestep);
            }

        }
    }
}
