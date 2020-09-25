package co2.framework.controller;

import co2.application.CO2Metric;
import co2.application.MetricsCollector;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MetricsCollectorController {
    private final MetricsCollector metricsCollector;

    @PostMapping("/sensors/{uuid}/mesurements")
    @ResponseStatus(HttpStatus.CREATED)
    public void collectMetric(@PathVariable("uuid") String uuid, @Valid @RequestBody CO2Metric co2Metric) {
        metricsCollector.collect(co2Metric.toBuilder().uuid(uuid).build());
    }
}
