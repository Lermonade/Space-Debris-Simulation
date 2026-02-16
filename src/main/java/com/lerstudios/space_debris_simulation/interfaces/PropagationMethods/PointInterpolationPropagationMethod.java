package com.lerstudios.space_debris_simulation.interfaces.PropagationMethods;

import com.lerstudios.space_debris_simulation.interfaces.Objects.PropagationDataObject;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PointInterpolationPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.SimplifiedVelocityVerletPropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.SimUtils;
import com.lerstudios.space_debris_simulation.utils.Constants;
import com.lerstudios.space_debris_simulation.utils.Vector3;

import java.util.Map;
import java.util.TreeMap;

public class PointInterpolationPropagationMethod implements PropagationMethod {
    @Override
    public void getPositionFromTime(PropagationDataObject object, double seconds) {
        PointInterpolationPropagationData data =
                (PointInterpolationPropagationData) object.getPropagationData();

        @SuppressWarnings("unchecked")
        Map<Double, Vector3> points =
                (Map<Double, Vector3>) data.getPropagationData();

        if (points == null || points.isEmpty()) {
            return;
        }

        // Sort by time
        TreeMap<Double, Vector3> sorted = new TreeMap<>(points);

        // Clamp to bounds
        if (seconds <= sorted.firstKey()) {
            Vector3 p = sorted.firstEntry().getValue();
            object.setPosition(
                    p.x * Constants.scaleFactor,
                    p.y * Constants.scaleFactor,
                    p.z * Constants.scaleFactor
            );
            return;
        }

        if (seconds >= sorted.lastKey()) {
            Vector3 p = sorted.lastEntry().getValue();
            object.setPosition(
                    p.x * Constants.scaleFactor,
                    p.y * Constants.scaleFactor,
                    p.z * Constants.scaleFactor
            );
            return;
        }

        Map.Entry<Double, Vector3> lower = sorted.floorEntry(seconds);
        Map.Entry<Double, Vector3> upper = sorted.ceilingEntry(seconds);

        if (lower == null || upper == null) {
            return;
        }

        double t0 = lower.getKey();
        double t1 = upper.getKey();

        if (t0 == t1) {
            Vector3 p = lower.getValue();
            object.setPosition(
                    p.x * Constants.scaleFactor,
                    p.y * Constants.scaleFactor,
                    p.z * Constants.scaleFactor
            );
            return;
        }

        double alpha = (seconds - t0) / (t1 - t0);

        Vector3 a = lower.getValue();
        Vector3 b = upper.getValue();

        double scale = Constants.scaleFactor;

        double x = (a.x + (b.x - a.x) * alpha) * scale;
        double y = (a.y + (b.y - a.y) * alpha) * scale;
        double z = (a.z + (b.z - a.z) * alpha) * scale;

        object.setPosition(x, y, z);
    }


    @Override
    public PropagationData setupObject(PropagationDataObject object, PropagationData data) {
        PointInterpolationPropagationData data2 = (PointInterpolationPropagationData) data;
        return new PointInterpolationPropagationData((Map<Double, Vector3>) data2.getPropagationData());
    }
}
