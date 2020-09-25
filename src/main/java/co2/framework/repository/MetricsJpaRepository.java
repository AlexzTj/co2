package co2.framework.repository;

import co2.application.MetricsRepository;
import co2.application.SensorAlert;
import co2.domain.CO2MetricEntity;
import co2.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface MetricsJpaRepository extends MetricsRepository, JpaRepository<CO2MetricJpaEntity, String> {
    @Override
    default void saveMetric(CO2MetricEntity metricEntity) {
        save(new CO2MetricJpaEntity(metricEntity.getUuid(),
                metricEntity.getCo2(), metricEntity.getTime(), metricEntity.getStatus()));
    }

    @Override
    default List<Status> findLastThreeSensorStatuses(String uuid) {
        List<CO2MetricJpaEntity> result = findTop3ByUuidOrderByTimeDesc(uuid);
        return result.stream().map(CO2MetricJpaEntity::getStatus).collect(Collectors.toList());
    }

    List<CO2MetricJpaEntity> findTop3ByUuidOrderByTimeDesc(String uuid);

    @Override
    default Integer findMaxSensorMetricValueForGivenPeriod(String id, Integer days) {

        return findTopByUuidAndTimeGreaterThanEqualOrderByCo2Desc(id, LocalDateTime.now().minusDays(days))
                .map(e->e.getCo2()).orElse(0);
    }

    Optional<CO2MetricJpaEntity> findTopByUuidAndTimeGreaterThanEqualOrderByCo2Desc(String id, LocalDateTime time);

    @Override
    default Double findAvgSensorMetricValueForGivenPeriod(String id, Integer days) {
         return findAvgByUuidAndTimeGreaterThanEqual(id,LocalDateTime.now().minusDays(days));
    }

    @Query("select AVG(e.co2) from CO2MetricJpaEntity e where e.uuid=?1 and e.time>=?2 ")
    Double findAvgByUuidAndTimeGreaterThanEqual(String id, LocalDateTime time);

    @Override
    default List<CO2MetricEntity> findAll(String id){
        return findAllByUuidOrderByTimeAsc(id).stream().map(e->new CO2MetricEntity(e.getUuid(),e.getCo2(),e.getTime()))
                .collect(Collectors.toList());
    }

    List<CO2MetricJpaEntity> findAllByUuidOrderByTimeAsc(String id);
}
