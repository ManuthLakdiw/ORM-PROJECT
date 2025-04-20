package lk.ijse.orm.ormproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "programme_table")
public class Programme implements SuperEntity {

    @Id
    private String id;
    private String programmeName;
    private String duration;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fee;

    @OneToMany(mappedBy = "programme")
    private List<Therapist> therapists;

    @OneToMany (mappedBy = "programme")
    private List<TherapySession> therapySessions;
}
