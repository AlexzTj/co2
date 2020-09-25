package co2.framework.controller;

import co2.application.SensorService;
import co2.application.SensorAlert;
import co2.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SensorController {
    private final SensorService sensorService;

    @GetMapping("/sensors/{uuid}")
    public StatusResponse getStatus(@PathVariable("uuid") String uuid) {
        Status sensorStatus = sensorService.getSensorStatus(uuid);
        return new StatusResponse(sensorStatus);
    }
    @GetMapping("/sensors/{uuid}/metrics")
    public MetricsResponse getMetrics(@PathVariable("uuid") String uuid) {
        Double avg = sensorService.getAvgSensorMetricValueForGivenPeriod(uuid, 30);
        Integer max = sensorService.getMaxSensorMetricValueForGivenPeriod(uuid, 30);
        return new MetricsResponse(max,avg);
    }
    @GetMapping("/sensors/{uuid}/alerts")
    public List<SensorAlert> getAlerts(@PathVariable("uuid") String uuid) {
       return sensorService.getSensorAlerts(uuid);
    }
}
