package co2.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@EqualsAndHashCode
public class CO2MetricEntity {
    @Setter
    private String uuid;
    @Setter
    private Integer co2;
    @Setter
    private LocalDateTime time;
    private Status status;

    public CO2MetricEntity(String uuid, Integer co2, LocalDateTime time) {
        this.uuid = uuid;
        this.co2 = co2;
        this.time = time;
        resolveStatus();
    }

    private void resolveStatus() {
        if (co2 > 2000) {
            status= Status.WARN;
        } else {
            status= Status.OK;
        }
    }
}
