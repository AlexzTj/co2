package co2.framework.repository;

import co2.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "metrics")
public class CO2MetricJpaEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private String uuid;
    private Integer co2;
    private LocalDateTime time;
    @Enumerated(value = EnumType.STRING)
    private Status status;

    public CO2MetricJpaEntity(String uuid, Integer co2, LocalDateTime time, Status status) {
        this.uuid = uuid;
        this.co2 = co2;
        this.time = time;
        this.status = status;
    }
}
