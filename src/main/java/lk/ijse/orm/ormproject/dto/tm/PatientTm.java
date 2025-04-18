package lk.ijse.orm.ormproject.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

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

public class PatientTm {
    private String id;
    private String title;
    private String name;
    private LocalDate dob;
    private String homeAddress;
    private String telephone;
    private String emailAddress;
    private Button updateBtn;
    private Button deleteBtn;
    private Button profileBtn;

}
