package co2.framework.repository;

import co2.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SensorCurrentStatus {
    @Id
    private String uuid;
    @Enumerated(value = EnumType.STRING)
    private Status status;
}
