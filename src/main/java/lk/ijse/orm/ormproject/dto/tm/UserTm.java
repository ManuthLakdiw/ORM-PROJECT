package lk.ijse.orm.ormproject.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserTm {
    private String id;
    private String name;
    private String password;
    private String role;
    private Button updateBtn;
    private Button deleteBtn;
}
