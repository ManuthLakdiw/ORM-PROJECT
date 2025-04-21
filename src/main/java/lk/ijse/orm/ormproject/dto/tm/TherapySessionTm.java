package lk.ijse.orm.ormproject.dto.tm;

import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import javafx.scene.control.Button;
import lk.ijse.orm.ormproject.entity.Programme;
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
public class TherapySessionTm {
    private String id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String therapist;
    private String programme;
    private Button btnUpdate;
    private Button btnDelete;
}
