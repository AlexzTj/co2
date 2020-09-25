package co2.application;

import co2.domain.CO2MetricEntity;
import co2.domain.CO2SensorUpdate;
import co2.domain.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MetricsCollectorTest {
    @Mock
    private MetricsRepository metricsRepository;
    @Mock
    private MetricUpdateListener metricEventPublisher;
    @InjectMocks
    private MetricsCollector target;

    @Test
    void when_ReceivedMetric_SaveIt() {
        CO2Metric metric = new CO2Metric(UUID.randomUUID().toString(), 1000, LocalDateTime.now());
        target.collect(metric);

        verify(metricsRepository).saveMetric(new CO2MetricEntity(metric.getUuid(), metric.getCo2(), metric.getTime()));
        verify(metricEventPublisher).onUpdate(new CO2SensorUpdate(metric.getUuid(), Status.OK));
    }

}
