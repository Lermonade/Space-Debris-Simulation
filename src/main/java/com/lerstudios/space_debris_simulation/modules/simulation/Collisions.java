package com.lerstudios.space_debris_simulation.modules.simulation;

import com.lerstudios.space_debris_simulation.interfaces.Objects.PropagationDataObject;
import com.lerstudios.space_debris_simulation.utils.Constants;
import com.lerstudios.space_debris_simulation.utils.Vector3;

public class Collisions {
    public static boolean checkForPlanetaryCollision(PropagationDataObject object) {

        double earthRadius = Constants.bodyRadius;

        double x = object.getX();
        double y = object.getY();
        double z = object.getZ();

        double distanceSquared = x * x + y * y + z * z;
        double radiusSquared = earthRadius * earthRadius;

        return distanceSquared <= radiusSquared;
    }

}
