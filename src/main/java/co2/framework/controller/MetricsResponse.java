package co2.framework.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class MetricsResponse {
 private Integer maxLast30Days;
 private Double avgLast30Days;
}
