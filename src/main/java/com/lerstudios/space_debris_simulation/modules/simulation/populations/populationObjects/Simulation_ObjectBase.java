package com.lerstudios.space_debris_simulation.modules.simulation.populations.populationObjects;

import com.lerstudios.space_debris_simulation.Console;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.StaticKeplerianPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.VisualizationData.VisualizationData;
import com.lerstudios.space_debris_simulation.modules.simulation.Collisions;
import com.lerstudios.space_debris_simulation.modules.simulation.SimUtils;
import com.lerstudios.space_debris_simulation.modules.visualization.Timing;
import com.lerstudios.space_debris_simulation.modules.visualization.populations.Objects.Visualization_ObjectRegistry;
import com.lerstudios.space_debris_simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;
import com.lerstudios.space_debris_simulation.types.TimedCoordinates;
import com.lerstudios.space_debris_simulation.utils.Constants;
import com.lerstudios.space_debris_simulation.utils.Vector3;

public class Simulation_ObjectBase implements Simulation_Object {
    private VisualizationData vdata;
    private PropagationData pdata;
    private PropegationMethod method;
    private boolean propagating = true;

    private boolean targeted = false;

    private Simulation_ObjectBase lastTargeter = null;

    private Vector3 position;
    private Vector3 velocity;
    private Vector3 addedAcceleration;

    private double mass;

    public TimedCoordinates propagationEndData = null;

    String name;
    ObjectClassification classification;

    public Simulation_ObjectBase(String name, ObjectClassification classification, PropegationMethod method) {
        this.name = name;
        this.classification = classification;
        this.method = method;

        this.position = new Vector3(0, 0, 0);
        this.velocity = new Vector3(0, 0, 0);
        this.addedAcceleration = new Vector3(0, 0, 0);

        Visualization_ObjectRegistry.register(this); //?
    }

    public void setVisualizationData(VisualizationData data) {
        this.vdata = data;
    }

    public VisualizationData getVisualizationData() {
        return this.vdata;
    }

    public void setPropagationData(PropagationData data) {
        this.pdata = data;
    }

    public PropagationData getPropagationData() {
        return this.pdata;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ObjectClassification getClassification() {
        return this.classification;
    }

    @Override
    public double getX() {
        return position.x;
    }

    @Override
    public double getY() {
        return position.y;
    }

    @Override
    public double getZ() {
        return position.z;
    }

    @Override
    public double getVx() {
        return velocity.x;
    }

    @Override
    public double getVy() {
        return velocity.y;
    }

    @Override
    public double getVz() {
        return velocity.z;
    }

    @Override
    public double getAx() {
        return addedAcceleration.x;
    }

    @Override
    public double getAy() {
        return addedAcceleration.y;
    }

    @Override
    public double getAz() {
        return addedAcceleration.z;
    }

    @Override
    public Vector3 getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    @Override
    public void setVelocity(double vx, double vy, double vz) {
        velocity.x = vx;
        velocity.y = vy;
        velocity.z = vz;
    }

    @Override
    public void setAddedAcceleration(double ax, double ay, double az) {
        addedAcceleration.x = ax;
        addedAcceleration.y = ay;
        addedAcceleration.z = az;
    }

    @Override
    public Vector3 getVelocity() {
        return this.velocity;
    }

    @Override
    public Vector3 getAddedAcceleration() {
        return addedAcceleration;
    }

    public void addAcceleration(Vector3 accel) {
        this.addedAcceleration.addInPlace(accel);
    }

    @Override
    public PropegationMethod getPropagationMethod() {
        return method;
    }

    public boolean isPropagating() {
        return propagating;
    }

    public void setPropagationMethod(PropegationMethod method) {
        this.method = method;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getMass() {
        return this.mass;
    }

    public void setLastTargeter(Simulation_ObjectBase targeter) {
        this.lastTargeter = targeter;
        this.targeted = true;
    }

    public boolean getWasTargeted() {
        return this.targeted;
    }

    public Simulation_ObjectBase getLastTargeter() {
        return this.lastTargeter;
    }

    public void propagate(double timestep, PropagationMethod propagator) {
        if(propagating) {
            propagator.getPositionFromTime(this, timestep * SimUtils.getSecondsPerTimeStep());

            if (Collisions.checkForPlanetaryCollision(this)) {
                //Constants.console.log("[" + timestep + "] " + this.name + " collided with the planet!");
                this.propagating = false;

                this.propagationEndData = TimedCoordinates.build(this, (int) timestep);
            }
        }
    }
}
