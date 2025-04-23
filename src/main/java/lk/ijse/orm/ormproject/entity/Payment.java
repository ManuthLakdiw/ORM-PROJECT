package lk.ijse.orm.ormproject.entity;

import jakarta.persistence.*;
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
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "payment_table")
public class Payment implements SuperEntity {

    @Id
    private String id;

    @OneToOne
    private Appointment appointment;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal sessionFee;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal paidAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal dueAmount;

    String status;

    LocalDate date;

    String patient;
}
