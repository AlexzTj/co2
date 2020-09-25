package co2.application;

import co2.domain.*;
import co2.domain.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {
    private final CO2SensorCurrentStatusRepository co2SensorCurrentStatusRepository;
    private final MetricsRepository metricsRepository;

    public Status getSensorStatus(String id) {
        return co2SensorCurrentStatusRepository.findStatusBySensorId(id)
                .orElseThrow(() -> new NotFoundException(new Error(ErrorCode.INVALID_SENSOR_ID)));
    }

    public Integer getMaxSensorMetricValueForGivenPeriod(String id, Integer days) {
        return metricsRepository.findMaxSensorMetricValueForGivenPeriod(id, days);
    }

    public Double getAvgSensorMetricValueForGivenPeriod(String id, Integer days) {
        return metricsRepository.findAvgSensorMetricValueForGivenPeriod(id, days);
    }

    public List<SensorAlert> getSensorAlerts(String id) {
        List<CO2MetricEntity> result = metricsRepository.findAll(id);
        List<CO2MetricEntity> curList = new ArrayList<>();
        List<SensorAlert> all = new ArrayList<>();

        for (CO2MetricEntity cur : result) {
            curList.add(cur);
            if (curList.size() == 3) {
                if (curList.stream().allMatch(e -> e.getStatus() == Status.WARN)) {
                    all.add(SensorAlert.from(curList));
                }
                curList.clear();
            }

        }
        return all;
    }

}
