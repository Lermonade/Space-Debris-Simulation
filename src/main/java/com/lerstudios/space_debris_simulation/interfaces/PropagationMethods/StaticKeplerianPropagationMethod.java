package com.lerstudios.space_debris_simulation.interfaces.PropagationMethods;

import com.lerstudios.space_debris_simulation.interfaces.Objects.PropagationDataObject;
import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;
import com.lerstudios.space_debris_simulation.utils.Constants;
import com.lerstudios.space_debris_simulation.utils.PropagationTools;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.StaticKeplerianPropagationData;
import org.fxyz3d.geometry.Point3D;

public class StaticKeplerianPropagationMethod implements PropagationMethod {
    boolean propagateVelocity = false;
    boolean smallScale = false;

    public StaticKeplerianPropagationMethod(boolean propagateVelocity, boolean smallScale) {
        this.propagateVelocity = propagateVelocity;
        this.smallScale = smallScale;
    }

    @Override
    public void getPositionFromTime(PropagationDataObject object, double seconds) {

        StaticKeplerianPropagationData data = (StaticKeplerianPropagationData) object.getPropagationData();
        RadianKeplerianElements elements = (RadianKeplerianElements) data.getPropagationData();

        // POSITION
        Point3D pos = PropagationTools.propagateOrbit(
                0, 0, 0,
                elements.semiMajorAxis(),
                elements.eccentricity(),
                elements.argPeriapsis(),
                elements.inclination(),
                elements.raan(),
                elements.anomaly(),
                seconds,
                Constants.gravitationalConstant * Constants.earthMass,
                true
        );

        if(smallScale) {
            object.setPosition(
                    pos.getX() * Constants.scaleFactor,
                    pos.getY() * Constants.scaleFactor,
                    pos.getZ() * Constants.scaleFactor
            );
        } else {
            object.setPosition(
                    pos.getX(),
                    pos.getY(),
                    pos.getZ()
            );
        }

        if (!propagateVelocity) {
            return;
        }

        // VELOCITY
        javafx.geometry.Point3D vel = PropagationTools.propagateOrbitVelocity(
                0, 0, 0,
                elements.semiMajorAxis(),
                elements.eccentricity(),
                elements.argPeriapsis(),
                elements.inclination(),
                elements.raan(),
                elements.anomaly(),
                seconds,
                Constants.gravitationalConstant * Constants.earthMass,
                true
        );

        object.setVelocity(vel.getX(), vel.getY(), vel.getZ());
    }


    @Override
    public PropagationData setupObject(PropagationDataObject object, PropagationData data) {
        StaticKeplerianPropagationData obj2 = (StaticKeplerianPropagationData) data;
        RadianKeplerianElements elements = (RadianKeplerianElements) obj2.getPropagationData();
        return new StaticKeplerianPropagationData(elements);
    }

    public RadianKeplerianElements getKeplerianData(PropagationDataObject object) {
        StaticKeplerianPropagationData data = (StaticKeplerianPropagationData) object.getPropagationData();
        RadianKeplerianElements elements = (RadianKeplerianElements) data.getPropagationData();
        return elements;
    }


}
