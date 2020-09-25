package co2.domain;

import lombok.Value;

@Value
public class CO2SensorUpdate {
    private final String uuid;
    private final Status status;
}
