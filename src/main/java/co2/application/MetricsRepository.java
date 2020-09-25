package co2.application;

import co2.domain.CO2MetricEntity;
import co2.domain.Status;

import java.util.List;

public interface MetricsRepository {
    void saveMetric(CO2MetricEntity metricEntity);

    List<Status> findLastThreeSensorStatuses(String uuid);

    Integer findMaxSensorMetricValueForGivenPeriod(String id, Integer days);

    Double findAvgSensorMetricValueForGivenPeriod(String id, Integer days);

    List<CO2MetricEntity> findAll(String id);
}
