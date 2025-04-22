package lk.ijse.orm.ormproject.dto.tm;

import javafx.scene.control.Button;
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

public class AppointmentTm {
    private String id;
    private String patient;
    LocalDate date;
    LocalTime time;
    private String session;
    private Button updateBtn;
    private Button deleteBtn;
}
