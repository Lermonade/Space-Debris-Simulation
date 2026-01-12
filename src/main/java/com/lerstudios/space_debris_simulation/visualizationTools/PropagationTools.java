package com.lerstudios.space_debris_simulation.visualizationTools;

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
            int resolution,
            boolean yUp) {

        if (eccentricity < 0 || eccentricity >= 1) {
            throw new IllegalArgumentException("Eccentricity must be in range [0, 1)");
        }

        List<Point3D> points = new ArrayList<>();

        double cosW = Math.cos(argumentOfPeriapsis);
        double sinW = Math.sin(argumentOfPeriapsis);
        double cosI = Math.cos(inclination);
        double sinI = Math.sin(inclination);
        double cosO = Math.cos(raOfAscendingNode);
        double sinO = Math.sin(raOfAscendingNode);

        for (int i = 0; i < resolution; i++) {
            double nu = 2 * Math.PI * i / resolution;

            // PQW Definition
            double r = semiMajorAxis * (1 - eccentricity * eccentricity)
                    / (1 + eccentricity * Math.cos(nu));

            double xOrb = r * Math.cos(nu);
            double yOrb = r * Math.sin(nu);
            double zOrb = 0;

            // Apply RAAN Ω about Z
            double x1 = xOrb * cosO - yOrb * sinO;
            double y1 = xOrb * sinO + yOrb * cosO;
            double z1 = zOrb;

            // Apply inclination i about X
            double x2 = x1;
            double y2 = y1 * cosI - z1 * sinI;
            double z2 = y1 * sinI + z1 * cosI;

            // Apply argument of periapsis ω about Z
            double x3 = x2 * cosW - y2 * sinW;
            double y3 = x2 * sinW + y2 * cosW;
            double z3 = z2;


            double finalX = focusX + x3;
            double finalY = focusY + y3;
            double finalZ = focusZ + z3;

            if (yUp) {
                double tmp = finalY;
                finalY = finalZ;
                finalZ = -tmp;
            }

            points.add(new Point3D(
                    (int) Math.round(finalX),
                    (int) Math.round(finalY),
                    (int) Math.round(finalZ)
            ));
        }

        points.add(points.getFirst());
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
}
