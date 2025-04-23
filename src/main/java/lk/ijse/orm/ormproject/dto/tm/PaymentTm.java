package lk.ijse.orm.ormproject.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

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
public class PaymentTm {
    private String id;
    private String appointment;
    private BigDecimal sessionFee;
    private BigDecimal paidAmount;
    private BigDecimal dueAmount;
    private String status;
    private LocalDate date;
    private String patient;
    private Button updateBtn;
    private Button deleteBtn;
}
