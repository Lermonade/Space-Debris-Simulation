package com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData;

import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;
import com.lerstudios.space_debris_simulation.types.TimedCoordinates;
import com.lerstudios.space_debris_simulation.utils.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimplifiedVelocityVerletPropagationData implements PropagationData {
    private Vector3 acceleration;
    private Vector3 pastAcceleration;
    private Vector3 addedAcceleration;

    private Map<Double, Vector3> visualizationPositions = new ConcurrentHashMap<>();

    public SimplifiedVelocityVerletPropagationData() {}

    public SimplifiedVelocityVerletPropagationData(Vector3 acceleration, Vector3 addedAcceleration) {
        this.acceleration = acceleration;
        this.pastAcceleration = acceleration;
        this.addedAcceleration = addedAcceleration;
    }

    @Override
    public Object getPropagationData() {
        return new AccelerationBundle(acceleration, pastAcceleration, addedAcceleration, visualizationPositions);
    }

    public void setAcceleration(Vector3 acceleration) {
        this.acceleration = acceleration;
    }

    public void setPastAcceleration(Vector3 acceleration) {
        this.pastAcceleration = acceleration;
    }

    public void setAddedAcceleration(Vector3 acceleration) {
        this.addedAcceleration = acceleration;
    }

    public Vector3 getAcceleration() {
        return this.acceleration;
    }

    public Vector3 getPastAcceleration() {
        return this.pastAcceleration;
    }

    public Vector3 getAddedAcceleration() {
        return this.addedAcceleration;
    }

    public void addVisualizationPosition(double time, Vector3 position) {
        visualizationPositions.put(time, new Vector3(position.x, position.y, position.z));
    }

    public static class AccelerationBundle {
        public Vector3 acceleration;
        public Vector3 pastAcceleration;
        public Vector3 addedAcceleration;
        public Map<Double, Vector3> points;

        public AccelerationBundle(Vector3 acceleration, Vector3 pastAcceleration, Vector3 addedAcceleration, Map<Double, Vector3> points) {
            this.acceleration = acceleration;
            this.pastAcceleration = pastAcceleration;
            this.addedAcceleration = addedAcceleration;
            this.points = points;
        }
    }
}
