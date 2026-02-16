package com.lerstudios.space_debris_simulation.types;

import com.lerstudios.space_debris_simulation.interfaces.Objects.Object3D;

public record TimedCoordinates(
        int timeStep,
        double x,
        double y,
        double z
) {

    public static TimedCoordinates build(Object3D o, int timeStep) {
        return new TimedCoordinates(
                timeStep,
                o.getX(),
                o.getY(),
                o.getZ()
        );
    }
}

