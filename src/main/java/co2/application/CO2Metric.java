package co2.application;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class CO2Metric {
    @JsonIgnore
    private String uuid;
    @NotNull
    private Integer co2;
    @NotNull
    private LocalDateTime time;
}
