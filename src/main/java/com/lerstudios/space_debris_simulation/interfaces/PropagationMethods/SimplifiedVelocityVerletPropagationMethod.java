package com.lerstudios.space_debris_simulation.interfaces.PropagationMethods;

import com.lerstudios.space_debris_simulation.interfaces.Objects.PropagationDataObject;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.SimplifiedVelocityVerletPropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.SimUtils;
import com.lerstudios.space_debris_simulation.modules.visualization.Timing;
import com.lerstudios.space_debris_simulation.utils.Constants;
import com.lerstudios.space_debris_simulation.utils.Vector3;

public class SimplifiedVelocityVerletPropagationMethod implements PropagationMethod {
    @Override
    public void getPositionFromTime(PropagationDataObject object, double seconds) {
        SimplifiedVelocityVerletPropagationData data = (SimplifiedVelocityVerletPropagationData) object.getPropagationData();
        data.setPastAcceleration(data.getAcceleration());

        Vector3 positionVector = positionVelocityVerlet(
                new Vector3(object.getX(), object.getY(), object.getZ()),
                new Vector3(object.getVx(), object.getVy(), object.getVz()),
                data.getPastAcceleration(),
                SimUtils.getSecondsPerTimeStep());

        object.setPosition(positionVector.x, positionVector.y, positionVector.z);
        if (SimUtils.getCreateVisualizationFile()) {
            data.addVisualizationPosition(SimUtils.getCurrentTimeStep() * SimUtils.getSecondsPerTimeStep(), object.getPosition());
        }

        Vector3 acceleration = gravitationalAcceleration(Constants.gravitationalConstant, Constants.earthMass, new Vector3(0, 0, 0), new Vector3(object.getX(), object.getY(), object.getZ()));
        acceleration.addInPlace(object.getAddedAcceleration());
        object.setAddedAcceleration(0, 0, 0);
        data.setAcceleration(acceleration);

        Vector3 velocityVector = velocityVelocityVerlet(
                new Vector3(object.getVx(), object.getVy(), object.getVz()),
                data.getPastAcceleration(),
                data.getAcceleration(),
                SimUtils.getSecondsPerTimeStep());
        object.setVelocity(velocityVector.x, velocityVector.y, velocityVector.z);
    }

    @Override
    public PropagationData setupObject(PropagationDataObject object, PropagationData data) {
        Vector3 acceleration = gravitationalAcceleration(Constants.gravitationalConstant, Constants.earthMass, new Vector3(0, 0, 0), new Vector3(object.getX(), object.getY(), object.getZ()));
        Vector3 addedAcceleration = new Vector3(0, 0, 0);
        SimplifiedVelocityVerletPropagationData data2 = (SimplifiedVelocityVerletPropagationData) data;
        data2.addVisualizationPosition(SimUtils.getCurrentTimeStep() * SimUtils.getSecondsPerTimeStep(), object.getPosition());
        return new SimplifiedVelocityVerletPropagationData(acceleration, addedAcceleration);
    }

    private static Vector3 positionVelocityVerlet(
            Vector3 position,
            Vector3 velocity,
            Vector3 acceleration,
            double timeStep
    ) {
        return new Vector3(
                position.x + velocity.x * timeStep + 0.5 * acceleration.x * timeStep * timeStep,
                position.y + velocity.y * timeStep + 0.5 * acceleration.y * timeStep * timeStep,
                position.z + velocity.z * timeStep + 0.5 * acceleration.z * timeStep * timeStep
        );
    }

    private static Vector3 velocityVelocityVerlet(
            Vector3 velocity,
            Vector3 previousAcceleration,
            Vector3 acceleration,
            double timeStep
    ) {
        return new Vector3(
                velocity.x + 0.5 * (previousAcceleration.x + acceleration.x) * timeStep,
                velocity.y + 0.5 * (previousAcceleration.y + acceleration.y) * timeStep,
                velocity.z + 0.5 * (previousAcceleration.z + acceleration.z) * timeStep
        );
    }

    private static Vector3 gravitationalAcceleration(
            double G,          // gravitational constant
            double massOther,  // mass of the body exerting gravity
            Vector3 positionOther, // position of the body exerting gravity
            Vector3 positionObject // position of the object being accelerated
    ) {
        // vector from object to the other body
        Vector3 rVec = new Vector3(
                positionOther.x - positionObject.x,
                positionOther.y - positionObject.y,
                positionOther.z - positionObject.z
        );

        double distanceSq = rVec.x * rVec.x + rVec.y * rVec.y + rVec.z * rVec.z;

        if (distanceSq == 0.0) {
            // objects are coincident; return zero acceleration to avoid NaN
            return new Vector3(0, 0, 0);
        }

        double distance = Math.sqrt(distanceSq);

        // acceleration magnitude
        double aMag = G * massOther / (distanceSq);

        // normalized direction
        Vector3 unitR = new Vector3(rVec.x / distance, rVec.y / distance, rVec.z / distance);

        // return acceleration vector
        return new Vector3(unitR.x * aMag, unitR.y * aMag, unitR.z * aMag);
    }

}
