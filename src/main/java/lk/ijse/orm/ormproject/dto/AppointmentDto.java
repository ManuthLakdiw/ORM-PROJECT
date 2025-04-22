package lk.ijse.orm.ormproject.dto;

import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lk.ijse.orm.ormproject.entity.Patient;
import lk.ijse.orm.ormproject.entity.TherapySession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

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
public class AppointmentDto {
    private String id;
    private String patient;
    LocalDate date;
    LocalTime time;
    private String session;
}
