package lk.ijse.orm.ormproject.dto.tm;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import javafx.scene.control.Button;
import lk.ijse.orm.ormproject.entity.Programme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TherapistTm {

    private String id;
    private String name;
    private String email;
    private String programme;
    private String telephone;
    private Button btnUpdate;
    private Button btnDelete;
}
