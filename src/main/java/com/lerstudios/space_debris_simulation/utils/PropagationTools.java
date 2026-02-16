package com.lerstudios.space_debris_simulation.utils;

import com.lerstudios.space_debris_simulation.interfaces.Objects.PropagationDataObject;
import com.lerstudios.space_debris_simulation.types.KeplerianElements;
import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.StaticKeplerianPropagationData;
import org.fxyz3d.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;

public class PropagationTools {
    public static List<Point3D> generateOrbitPoints(
            double focusX,
            double focusY,
            double focusZ,
            double semiMajorAxis,
            double eccentricity,
            double argumentOfPeriapsis,
            double inclination,
            double raOfAscendingNode,
            double initialTrueAnomaly,
            double mu,
            int resolution,
            boolean yUp
    ) {
        if (eccentricity < 0 || eccentricity >= 1) {
            throw new IllegalArgumentException("Eccentricity must be in range [0, 1).");
        }

        List<Point3D> points = new ArrayList<>();

        // Orbital period
        double n = Math.sqrt(mu / Math.pow(semiMajorAxis, 3)); // mean motion
        double period = 2 * Math.PI / n;

        // Step through orbit in time to generate points
        for (int i = 0; i < resolution; i++) {
            double t = i * period / resolution; // time at this point

            Point3D pos = propagateOrbit(
                    focusX,
                    focusY,
                    focusZ,
                    semiMajorAxis,
                    eccentricity,
                    argumentOfPeriapsis,
                    inclination,
                    raOfAscendingNode,
                    initialTrueAnomaly,
                    t,
                    mu,
                    yUp
            );

            points.add(pos);
        }

        // Close the loop
        points.add(points.get(0));

        return points;
    }


    public static Point3D propagateOrbit(
            double focusX,
            double focusY,
            double focusZ,
            double semiMajorAxis,        // a
            double eccentricity,         // e (0 <= e < 1)
            double argumentOfPeriapsis,  // ω (radians)
            double inclination,          // i (radians)
            double raOfAscendingNode,    // Ω (radians)
            double initialTrueAnomaly,   // ν0 at t=0
            double timeSeconds,          // t seconds after initial position
            double mu,                   // gravitational parameter of central body
            boolean yUp) {                  // true → convert physics Z-up → visualization Y-up

        if (eccentricity < 0 || eccentricity >= 1) {
            throw new IllegalArgumentException("Eccentricity must be in range [0, 1).");
        }

        // Mean Motion
        double n = Math.sqrt(mu / Math.pow(semiMajorAxis, 3));

        // Initial Eccentric Anomaly
        double cosE0 = (eccentricity + Math.cos(initialTrueAnomaly)) / (1 + eccentricity * Math.cos(initialTrueAnomaly));
        double sinE0 = Math.sqrt(1 - eccentricity * eccentricity) * Math.sin(initialTrueAnomaly) / (1 + eccentricity * Math.cos(initialTrueAnomaly));
        double E0 = Math.atan2(sinE0, cosE0);

        // Initial Mean Anomaly
        double M0 = E0 - eccentricity * Math.sin(E0);

        // Mean Anomaly at a time
        double M = M0 + n * timeSeconds;

        // Solve Kepler's equation M = E - e*sin(E) for eccentric anomaly E
        double E = M; // initial guess
        for (int iter = 0; iter < 20; iter++) {
            double f = E - eccentricity * Math.sin(E) - M;
            double fPrime = 1 - eccentricity * Math.cos(E);
            double delta = f / fPrime;
            E -= delta;
            if (Math.abs(delta) < 1e-10) break;
        }

        // True anomaly
        double cosNu = (Math.cos(E) - eccentricity) / (1 - eccentricity * Math.cos(E));
        double sinNu = Math.sqrt(1 - eccentricity * eccentricity) * Math.sin(E) / (1 - eccentricity * Math.cos(E));
        double nu = Math.atan2(sinNu, cosNu);

        // Radius r
        double r = semiMajorAxis * (1 - eccentricity * Math.cos(E));

        // Orbital plane coordinates
        double xOrb = r * Math.cos(nu);
        double yOrb = r * Math.sin(nu);
        double zOrb = 0;

        // Trig constants
        double cosO = Math.cos(raOfAscendingNode);
        double sinO = Math.sin(raOfAscendingNode);
        double cosI = Math.cos(inclination);
        double sinI = Math.sin(inclination);
        double cosW = Math.cos(argumentOfPeriapsis);
        double sinW = Math.sin(argumentOfPeriapsis);

        // Apply RAAN (Ω) rotation about Z
        double x1 = xOrb * cosO - yOrb * sinO;
        double y1 = xOrb * sinO + yOrb * cosO;
        double z1 = zOrb;

        // Apply inclination (i) about X
        double x2 = x1;
        double y2 = y1 * cosI - z1 * sinI;
        double z2 = y1 * sinI + z1 * cosI;

        // Apply argument of periapsis (ω) about Z
        double x3 = x2 * cosW - y2 * sinW;
        double y3 = x2 * sinW + y2 * cosW;
        double z3 = z2;

        // Translate to focus position
        double finalX = focusX + x3;
        double finalY = focusY + y3;
        double finalZ = focusZ + z3;

        // Convert Z-up → Y-up if requested
        if (yUp) {
            double tmp = finalY;
            finalY = finalZ;
            finalZ = -tmp;
        }

        return new Point3D(
                finalX,
                finalY,
                finalZ
        );
    }

    public static javafx.geometry.Point3D propagateOrbitVelocity(
            double focusX,
            double focusY,
            double focusZ,
            double semiMajorAxis,        // a
            double eccentricity,         // e (0 <= e < 1)
            double argumentOfPeriapsis,  // ω (radians)
            double inclination,          // i (radians)
            double raOfAscendingNode,    // Ω (radians)
            double initialTrueAnomaly,   // ν0 at t=0
            double timeSeconds,          // t seconds after initial position
            double mu,                   // gravitational parameter of central body
            boolean yUp                  // true → convert physics Z-up → visualization Y-up
    ) {
        if (eccentricity < 0 || eccentricity >= 1) {
            throw new IllegalArgumentException("Eccentricity must be in range [0, 1).");
        }

        // Mean Motion
        double n = Math.sqrt(mu / Math.pow(semiMajorAxis, 3));

        // Initial Eccentric Anomaly
        double cosE0 = (eccentricity + Math.cos(initialTrueAnomaly)) / (1 + eccentricity * Math.cos(initialTrueAnomaly));
        double sinE0 = Math.sqrt(1 - eccentricity * eccentricity) * Math.sin(initialTrueAnomaly) / (1 + eccentricity * Math.cos(initialTrueAnomaly));
        double E0 = Math.atan2(sinE0, cosE0);

        // Initial Mean Anomaly
        double M0 = E0 - eccentricity * Math.sin(E0);

        // Mean Anomaly at a time
        double M = M0 + n * timeSeconds;

        // Solve Kepler's equation M = E - e*sin(E)
        double E = M; // initial guess
        for (int iter = 0; iter < 20; iter++) {
            double f = E - eccentricity * Math.sin(E) - M;
            double fPrime = 1 - eccentricity * Math.cos(E);
            double delta = f / fPrime;
            E -= delta;
            if (Math.abs(delta) < 1e-10) break;
        }

        // True anomaly
        double cosNu = (Math.cos(E) - eccentricity) / (1 - eccentricity * Math.cos(E));
        double sinNu = Math.sqrt(1 - eccentricity * eccentricity) * Math.sin(E) / (1 - eccentricity * Math.cos(E));
        double nu = Math.atan2(sinNu, cosNu);

        // Radius
        double r = semiMajorAxis * (1 - eccentricity * Math.cos(E));

        // Specific angular momentum
        double h = Math.sqrt(mu * semiMajorAxis * (1 - eccentricity * eccentricity));

        // Radial and tangential velocities in orbital plane
        double vr = (mu / h) * eccentricity * Math.sin(nu);
        double vtheta = (mu / h) * (1 + eccentricity * Math.cos(nu));

        // Convert to PQW Cartesian coordinates
        double vxOrb = vr * Math.cos(nu) - vtheta * Math.sin(nu);
        double vyOrb = vr * Math.sin(nu) + vtheta * Math.cos(nu);
        double vzOrb = 0;

        // Rotation constants
        double cosO = Math.cos(raOfAscendingNode);
        double sinO = Math.sin(raOfAscendingNode);
        double cosI = Math.cos(inclination);
        double sinI = Math.sin(inclination);
        double cosW = Math.cos(argumentOfPeriapsis);
        double sinW = Math.sin(argumentOfPeriapsis);

        // Rotate same as position
        double vx1 = vxOrb * cosO - vyOrb * sinO;
        double vy1 = vxOrb * sinO + vyOrb * cosO;
        double vz1 = vzOrb;

        double vx2 = vx1;
        double vy2 = vy1 * cosI - vz1 * sinI;
        double vz2 = vy1 * sinI + vz1 * cosI;

        double vx3 = vx2 * cosW - vy2 * sinW;
        double vy3 = vx2 * sinW + vy2 * cosW;
        double vz3 = vz2;

        // Convert Z-up → Y-up if requested
        double finalVX = vx3;
        double finalVY = vy3;
        double finalVZ = vz3;
        if (yUp) {
            double tmp = finalVY;
            finalVY = finalVZ;
            finalVZ = -tmp;
        }

        return new javafx.geometry.Point3D(finalVX, finalVY, finalVZ);
    }


    public static javafx.geometry.Point3D getVelocityVector(PropagationDataObject object, double seconds) {
        StaticKeplerianPropagationData data = (StaticKeplerianPropagationData) object.getPropagationData();
        RadianKeplerianElements elements = (RadianKeplerianElements) data.getPropagationData();

        // Propagate orbit with velocity calculation enabled
        return PropagationTools.propagateOrbitVelocity(0, 0, 0,
                elements.semiMajorAxis(), elements.eccentricity(), elements.argPeriapsis(),
                elements.inclination(), elements.raan(), elements.anomaly(),
                seconds, Constants.gravitationalConstant * Constants.earthMass, true);
        // Returns velocity vector in same units as position (apply scale factor if needed)
    }

    public static KeplerianElements cartesianToKeplerian(double[] r, double[] v, double mu, double mass) {
        // r and v are 3-element arrays: [x, y, z] and [vx, vy, vz]
        // mu = GM of central body

        // Compute magnitudes
        double rMag = Math.sqrt(r[0]*r[0] + r[1]*r[1] + r[2]*r[2]);
        double vMag = Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);

        // Specific angular momentum vector h = r x v
        double[] h = new double[3];
        h[0] = r[1]*v[2] - r[2]*v[1];
        h[1] = r[2]*v[0] - r[0]*v[2];
        h[2] = r[0]*v[1] - r[1]*v[0];
        double hMag = Math.sqrt(h[0]*h[0] + h[1]*h[1] + h[2]*h[2]);

        // Inclination
        double i = Math.acos(h[2]/hMag);

        // Node vector n = k x h
        double[] k = {0,0,1};
        double[] n = new double[3];
        n[0] = k[1]*h[2] - k[2]*h[1];
        n[1] = k[2]*h[0] - k[0]*h[2];
        n[2] = k[0]*h[1] - k[1]*h[0];
        double nMag = Math.sqrt(n[0]*n[0] + n[1]*n[1]);

        // Eccentricity vector
        double[] eVec = new double[3];
        double rDotV = r[0]*v[0] + r[1]*v[1] + r[2]*v[2];
        for (int j=0;j<3;j++) {
            eVec[j] = (1/mu)*((vMag*vMag - mu/rMag)*r[j] - rDotV*v[j]);
        }
        double e = Math.sqrt(eVec[0]*eVec[0] + eVec[1]*eVec[1] + eVec[2]*eVec[2]);

        // RAAN
        double raan = Math.acos(n[0]/nMag);
        if (n[1] < 0) raan = 2*Math.PI - raan;

        // Argument of periapsis
        double argPeri = Math.acos((n[0]*eVec[0] + n[1]*eVec[1] + n[2]*eVec[2]) / (nMag*e));
        if (eVec[2] < 0) argPeri = 2*Math.PI - argPeri;

        // True anomaly
        double trueAnomaly = Math.acos((eVec[0]*r[0] + eVec[1]*r[1] + eVec[2]*r[2]) / (e*rMag));
        if (rDotV < 0) trueAnomaly = 2*Math.PI - trueAnomaly;

        // Semi-major axis
        double a = 1 / (2/rMag - vMag*vMag/mu);

        // Convert angles to degrees
        i = Math.toDegrees(i);
        raan = Math.toDegrees(raan);
        argPeri = Math.toDegrees(argPeri);
        trueAnomaly = Math.toDegrees(trueAnomaly);

        // Build KeplerianElements object
        return new KeplerianElements(a, e, i, raan, argPeri, trueAnomaly, mass);
    }


}
