package co2.application;

import co2.domain.Status;

import java.util.Optional;

public interface CO2SensorCurrentStatusRepository {
    Optional<Status> findStatusBySensorId(String uuid);

    void save(String uuid, Status status);
}
