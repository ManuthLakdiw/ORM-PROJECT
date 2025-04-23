package lk.ijse.orm.ormproject.dto;

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
public class PaymentDto {

    private String id;
    private String appointment;
    private BigDecimal sessionFee;
    private BigDecimal paidAmount;
    private BigDecimal dueAmount;
    private String status;
    private LocalDate date;
    private String patient;
}
