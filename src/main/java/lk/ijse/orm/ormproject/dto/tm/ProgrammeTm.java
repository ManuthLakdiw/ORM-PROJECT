package lk.ijse.orm.ormproject.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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

public class ProgrammeTm {

    private String id;
    private String programmeName;
    private String duration;
    private BigDecimal fee;
    private Button updateBtn;
    private Button deleteBtn;

}
