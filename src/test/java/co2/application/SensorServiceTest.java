package co2.application;

import co2.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class SensorServiceTest {
    @Mock
    private  CO2SensorCurrentStatusRepository co2SensorCurrentStatusRepository;
    @Mock
    private MetricsRepository metricsRepository;
    @InjectMocks
    private SensorService target;

    @Test
     void getSensorStatus(){
        String id="id";
        when(co2SensorCurrentStatusRepository.findStatusBySensorId(id)).thenReturn(Optional.of(Status.OK));
        Status sensorStatus = target.getSensorStatus(id);

        assertThat(sensorStatus).isEqualTo(Status.OK);
    }

   @Test
    void getMaxSensorMetricValueForGivenPeriod(){
       String id="id";
       Integer days=30;
       target.getMaxSensorMetricValueForGivenPeriod(id,days);

       verify(metricsRepository).findMaxSensorMetricValueForGivenPeriod(id,days);
    }
    @Test
    void getAvgSensorMetricValueForGivenPeriod(){
        String id="id";
        Integer days=30;
        target.getAvgSensorMetricValueForGivenPeriod(id,days);

        verify(metricsRepository).findAvgSensorMetricValueForGivenPeriod(id,days);
    }
    @Test
    void getSensorAlerts(){
        String id = "id";
        CO2MetricEntity c1=new CO2MetricEntity(id,3000, LocalDateTime.now());
        CO2MetricEntity c2=new CO2MetricEntity(id,3000, LocalDateTime.now());
        CO2MetricEntity c3=new CO2MetricEntity(id,3000, LocalDateTime.now());

        CO2MetricEntity c4=new CO2MetricEntity(id,1000, LocalDateTime.now());
        CO2MetricEntity c5=new CO2MetricEntity(id,3000, LocalDateTime.now());
        CO2MetricEntity c6=new CO2MetricEntity(id,3000, LocalDateTime.now());

        CO2MetricEntity c7=new CO2MetricEntity(id,4000, LocalDateTime.now());
        CO2MetricEntity c8=new CO2MetricEntity(id,4000, LocalDateTime.now());
        CO2MetricEntity c9=new CO2MetricEntity(id,4000, LocalDateTime.now());

        when(metricsRepository.findAll(id)).thenReturn(List.of(c1,c2,c3,c4,c5,c6,c7,c8,c9));
        List<SensorAlert> alerts = target.getSensorAlerts(id);

        assertThat(alerts).containsExactly(new SensorAlert(c1.getTime(), c3.getTime(), c1.getCo2(), c2.getCo2(), c3.getCo2()),
                new SensorAlert(c7.getTime(), c9.getTime(), c7.getCo2(), c8.getCo2(), c9.getCo2()));
    }
}
