package com.lerstudios.space_debris_simulation.interfaces.Objects;

import com.lerstudios.space_debris_simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.utils.Vector3;

public interface Object3D {
    String getName();
    ObjectClassification getClassification();

    // Position getters
    double getX();
    double getY();
    double getZ();

    // Velocity getters
    double getVx();
    double getVy();
    double getVz();

    // Acceleration getters
    double getAx();
    double getAy();
    double getAz();

    Vector3 getPosition();
    Vector3 getVelocity();
    Vector3 getAddedAcceleration();

    // Optional setters if you want mutability
    void setPosition(double x, double y, double z);
    void setVelocity(double vx, double vy, double vz);
    void setAddedAcceleration(double ax, double ay, double az);

}
