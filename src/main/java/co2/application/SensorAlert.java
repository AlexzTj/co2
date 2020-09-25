package co2.application;

import co2.domain.CO2MetricEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SensorAlert {
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Integer mesurement1;
  private Integer mesurement2;
  private Integer mesurement3;

  public static SensorAlert from(List<CO2MetricEntity> curList) {
    LocalDateTime min = curList.get(0).getTime();
    LocalDateTime max = curList.get(curList.size()-1).getTime();
    return new SensorAlert(min,max,curList.get(0).getCo2(),curList.get(1).getCo2(),curList.get(2).getCo2());
  }
}
