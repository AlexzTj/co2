package co2.application;

import co2.domain.CO2SensorUpdate;
import co2.domain.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static co2.domain.Status.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricUpdateListenerTest {
    @Mock
    private MetricsRepository metricsRepository;
    @Mock
    private CO2SensorCurrentStatusRepository co2SensorCurrentStatusRepository;
    @InjectMocks
    private MetricUpdateListener target;

    private static Stream<Arguments> provideValues() {
        return Stream.of(
                Arguments.of(List.of(WARN, WARN, WARN), WARN, ALERT),
                Arguments.of(List.of(OK, OK, OK), ALERT, OK),
                Arguments.of(List.of(OK, OK, OK), WARN, OK)
        );
    }

    private static Stream<Arguments> provideValuesSet2() {
        return Stream.of(
                Arguments.of(List.of(OK), OK, OK),
                Arguments.of(List.of(WARN), WARN, OK, WARN),
                Arguments.of(List.of(OK, OK), WARN, WARN),
                Arguments.of(List.of(WARN, OK, OK), ALERT, OK, ALERT)

        );
    }

    private static Stream<Arguments> provideValuesSet3() {
        return Stream.of(
                Arguments.of(OK),
                Arguments.of(WARN)
        );
    }

    @ParameterizedTest
    @MethodSource("provideValues")
    void onUpdate(List<Status> last3Statuses, Status existingStatus, Status expectedStatus) {
        when(metricsRepository.findLastThreeSensorStatuses("id")).thenReturn(last3Statuses);
        when(co2SensorCurrentStatusRepository.findStatusBySensorId("id")).thenReturn(Optional.ofNullable(existingStatus));
        CO2SensorUpdate update = new CO2SensorUpdate("id", null);
        target.onUpdate(update);

        verify(co2SensorCurrentStatusRepository).save("id", expectedStatus);
    }

    @ParameterizedTest
    @MethodSource("provideValuesSet2")
    void onUpdateStatusIsNotChanged(List<Status> last3Statuses, Status existingStatus) {
        when(metricsRepository.findLastThreeSensorStatuses("id")).thenReturn(last3Statuses);
        when(co2SensorCurrentStatusRepository.findStatusBySensorId("id")).thenReturn(Optional.ofNullable(existingStatus));
        CO2SensorUpdate update = new CO2SensorUpdate("id", null);
        target.onUpdate(update);

        verify(co2SensorCurrentStatusRepository, times(0)).save(any(), any());
    }

    @ParameterizedTest
    @MethodSource("provideValuesSet3")
    void onUpdateStatusIsInitializedFirstTime(Status currentStatus) {
        when(metricsRepository.findLastThreeSensorStatuses("id")).thenReturn(List.of());
        when(co2SensorCurrentStatusRepository.findStatusBySensorId("id")).thenReturn(Optional.empty());
        CO2SensorUpdate update = new CO2SensorUpdate("id", currentStatus);
        target.onUpdate(update);

        verify(co2SensorCurrentStatusRepository).save("id", currentStatus);
    }
}