package com.lerstudios.space_debris_simulation.types;

public record KeplerianElements(
        double semiMajorAxis,
        double eccentricity,
        double inclination,
        double raan,
        double argPeriapsis,
        double anomaly,
        double mass
) {}
