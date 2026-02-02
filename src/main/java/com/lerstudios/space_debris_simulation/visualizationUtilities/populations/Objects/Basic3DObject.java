package com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects;

import com.lerstudios.space_debris_simulation.simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.utils.Vector3;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.VisualizationData.VisualizationData;

public class Basic3DObject implements Object{
    private VisualizationData vdata;
    private PropagationData pdata;

    private Vector3 position;
    private Vector3 velocity;

    String name;
    ObjectClassification classification;

    public Basic3DObject(String name, ObjectClassification classification) {
        this.name = name;
        this.classification = classification;

        this.position = new Vector3(0, 0, 0);
        this.velocity = new Vector3(0, 0, 0);
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
}
