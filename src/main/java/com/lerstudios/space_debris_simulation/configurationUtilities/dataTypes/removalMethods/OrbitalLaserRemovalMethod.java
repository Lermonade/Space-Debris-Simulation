package com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.removalMethods;

public class OrbitalLaserRemovalMethod implements RemovalMethodTemplate{

    public String name;

    public String laserPowerWatts;          // W
    public String wavelengthNanometers;     // nm
    public String apertureDiameterMeters;   // m
    public String beamQualityM2;             // unitless
    public String opticalEfficiencyPercent;  // %

    public String pulseRateHz;               // Hz
    public String pulseDurationPicoseconds;  // ps

    public String maxEngagementTimeSeconds;  // s
    public String cooldownTimeSeconds;       // s
    public String slewRateDegPerSec;          // deg/s
    public String targetingRangeMeters;      // m

    public OrbitalLaserRemovalMethod() {}

    public OrbitalLaserRemovalMethod(
            String name,

            String laserPowerWatts,
            String wavelengthNanometers,
            String apertureDiameterMeters,
            String beamQualityM2,
            String opticalEfficiencyPercent,

            String pulseRateHz,
            String pulseDurationPicoseconds,

            String maxEngagementTimeSeconds,
            String cooldownTimeSeconds,
            String slewRateDegPerSec,
            String targetingRangeMeters
    ) {
        this.name = name;

        this.laserPowerWatts = laserPowerWatts;
        this.wavelengthNanometers = wavelengthNanometers;
        this.apertureDiameterMeters = apertureDiameterMeters;
        this.beamQualityM2 = beamQualityM2;
        this.opticalEfficiencyPercent = opticalEfficiencyPercent;

        this.pulseRateHz = pulseRateHz;
        this.pulseDurationPicoseconds = pulseDurationPicoseconds;

        this.maxEngagementTimeSeconds = maxEngagementTimeSeconds;
        this.cooldownTimeSeconds = cooldownTimeSeconds;
        this.slewRateDegPerSec = slewRateDegPerSec;
        this.targetingRangeMeters = targetingRangeMeters;
    }

    @Override
    public RemovalMethodType getRemovalMethodType() {
        return RemovalMethodType.ORBITAL_LASER;
    }
}
