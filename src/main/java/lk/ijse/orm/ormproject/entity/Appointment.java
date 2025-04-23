package lk.ijse.orm.ormproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "appointment_table")
public class Appointment implements SuperEntity {
    @Id
    private String id;
    LocalTime time;
    LocalDate date;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private TherapySession therapySession;

    @OneToOne (mappedBy = "appointment")
    private Payment payment;
}
