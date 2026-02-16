package com.lerstudios.space_debris_simulation.types;

public record TimedKeplerianElements(
        KeplerianElements elements,
        double startTimeStep,
        double endTimeStep
) {}

