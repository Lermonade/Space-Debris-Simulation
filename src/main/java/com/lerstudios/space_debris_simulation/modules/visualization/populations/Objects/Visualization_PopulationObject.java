package com.lerstudios.space_debris_simulation.modules.visualization.populations.Objects;

import com.lerstudios.space_debris_simulation.interfaces.Objects.Object3D;
import com.lerstudios.space_debris_simulation.interfaces.Objects.PropagationDataObject;
import com.lerstudios.space_debris_simulation.interfaces.Objects.VisualizationDataObject;
import com.lerstudios.space_debris_simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.types.TimedCoordinates;
import com.lerstudios.space_debris_simulation.utils.Vector3;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.VisualizationData.VisualizationData;

public class Visualization_PopulationObject implements Object3D, PropagationDataObject, VisualizationDataObject {
    private VisualizationData vdata;
    private PropagationData pdata;

    private Vector3 position;
    private Vector3 velocity;
    private Vector3 addedAcceleration;

    public TimedCoordinates endData = null;

    String name;
    ObjectClassification classification;

    public Visualization_PopulationObject(String name, ObjectClassification classification) {
        this.name = name;
        this.classification = classification;

        this.position = new Vector3(0, 0, 0);
        this.velocity = new Vector3(0, 0, 0);

        Visualization_ObjectRegistry.register(this);
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
}
