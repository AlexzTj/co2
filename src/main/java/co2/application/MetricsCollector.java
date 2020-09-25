package co2.application;

import co2.domain.CO2MetricEntity;
import co2.domain.CO2SensorUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MetricsCollector {
    private final MetricsRepository metricsRepository;
    private final MetricUpdateListener metricUpdateListener;

    @Transactional
    public void collect(CO2Metric metric) {
        CO2MetricEntity metricEntity = new CO2MetricEntity(metric.getUuid(), metric.getCo2(), metric.getTime());
        metricsRepository.saveMetric(metricEntity);
        metricUpdateListener.onUpdate(new CO2SensorUpdate(metric.getUuid(),metricEntity.getStatus()));
    }


}
