package lk.ijse.orm.ormproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
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
@Table(name = "session_schedule")
public class TherapySession implements SuperEntity {

    @Id
    private String id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String therapist;

    @ManyToOne
    private Programme programme;

    @OneToMany (mappedBy = "therapySession")
    private List<Appointment> appointments;


}
