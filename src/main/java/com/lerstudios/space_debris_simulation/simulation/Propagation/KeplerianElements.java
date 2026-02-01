package com.lerstudios.space_debris_simulation.simulation.Propagation;

public record KeplerianElements(
        double semiMajorAxis,
        double eccentricity,
        double inclination,
        double raan,
        double argPeriapsis,
        double anomaly,
        double mass
) {}
