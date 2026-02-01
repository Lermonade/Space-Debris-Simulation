package com.lerstudios.space_debris_simulation.simulation.Propagation;

public record TimedKeplerianElements(
        KeplerianElements elements,
        double startTimeStep,
        double endTimeStep
) {}

