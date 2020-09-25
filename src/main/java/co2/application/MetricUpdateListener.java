package co2.application;

import co2.domain.CO2SensorUpdate;
import co2.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class MetricUpdateListener {
    private final MetricsRepository metricsRepository;
    private final CO2SensorCurrentStatusRepository co2SensorCurrentStatusRepository;

    public void onUpdate(CO2SensorUpdate co2SensorUpdate) {
        Optional<Status> currentStatusOpt = co2SensorCurrentStatusRepository.findStatusBySensorId(co2SensorUpdate.getUuid());
        List<Status> last3Statuses = metricsRepository.findLastThreeSensorStatuses(co2SensorUpdate.getUuid());

        if (currentStatusOpt.isEmpty()) {
            co2SensorCurrentStatusRepository.save(co2SensorUpdate.getUuid(), co2SensorUpdate.getStatus());
            return;
        }

        Status currentStatus = currentStatusOpt.get();
        if (currentStatus == Status.OK && co2SensorUpdate.getStatus() == Status.WARN) {
            co2SensorCurrentStatusRepository.save(co2SensorUpdate.getUuid(), Status.WARN);
        }
        if (last3Statuses.size() == 3) {
            if (Status.allWarn(last3Statuses)) {
                co2SensorCurrentStatusRepository.save(co2SensorUpdate.getUuid(), Status.ALERT);
            } else if (Status.allOk(last3Statuses)) {
                co2SensorCurrentStatusRepository.save(co2SensorUpdate.getUuid(), Status.OK);
            }
        }
    }

}
