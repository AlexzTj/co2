package co2.framework.repository;

import co2.application.CO2SensorCurrentStatusRepository;
import co2.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CO2SensorCurrentStatusJpaRepository extends CO2SensorCurrentStatusRepository, JpaRepository<SensorCurrentStatus,String> {
    @Override
    default Optional<Status> findStatusBySensorId(String uuid) {
        return findById(uuid).map(e->e.getStatus());
    }

    @Override
    default void save(String uuid, Status status){
        save(new SensorCurrentStatus(uuid,status));
    }
}
