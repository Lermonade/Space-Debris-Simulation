package com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.orbitSources;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class KeplerianDistributionSource implements SourceTemplate {
    public String sourceName;

    public String semiMajorAxisMin;
    public String semiMajorAxisAvg;
    public String semiMajorAxisMax;

    public String eccentricityMin;
    public String eccentricityAvg;
    public String eccentricityMax;

    public String inclinationMin;
    public String inclinationAvg;
    public String inclinationMax;

    public String raanMin;
    public String raanAvg;
    public String raanMax;

    public String argPeriapsisMin;
    public String argPeriapsisAvg;
    public String argPeriapsisMax;

    public String anomalyMin;
    public String anomalyAvg;
    public String anomalyMax;

    public String massMin;
    public String massAvg;
    public String massMax;

    public KeplerianDistributionSource() {
    }

    public KeplerianDistributionSource(
            String sourceName,

            String semiMajorAxisMin,
            String semiMajorAxisAvg,
            String semiMajorAxisMax,

            String eccentricityMin,
            String eccentricityAvg,
            String eccentricityMax,

            String inclinationMin,
            String inclinationAvg,
            String inclinationMax,

            String raanMin,
            String raanAvg,
            String raanMax,

            String argPeriapsisMin,
            String argPeriapsisAvg,
            String argPeriapsisMax,

            String anomalyMin,
            String anomalyAvg,
            String anomalyMax,

            String massMin,
            String massAvg,
            String massMax
    ) {
        this.sourceName = sourceName;

        this.semiMajorAxisMin = semiMajorAxisMin;
        this.semiMajorAxisAvg = semiMajorAxisAvg;
        this.semiMajorAxisMax = semiMajorAxisMax;

        this.eccentricityMin = eccentricityMin;
        this.eccentricityAvg = eccentricityAvg;
        this.eccentricityMax = eccentricityMax;

        this.inclinationMin = inclinationMin;
        this.inclinationAvg = inclinationAvg;
        this.inclinationMax = inclinationMax;

        this.raanMin = raanMin;
        this.raanAvg = raanAvg;
        this.raanMax = raanMax;

        this.argPeriapsisMin = argPeriapsisMin;
        this.argPeriapsisAvg = argPeriapsisAvg;
        this.argPeriapsisMax = argPeriapsisMax;

        this.anomalyMin = anomalyMin;
        this.anomalyAvg = anomalyAvg;
        this.anomalyMax = anomalyMax;

        this.massMin = massMin;
        this.massAvg = massAvg;
        this.massMax = massMax;
    }

    @Override
    @JsonIgnore
    public SourceType getSourceType() {
        return SourceType.KEPLERIAN;
    }
}
