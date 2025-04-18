package lk.ijse.orm.ormproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "patient_table")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Patient implements SuperEntity {
    @Id
    private String id;
    private String title;
    private String name;
    private LocalDate dob;
    private String homeAddress;
    private String telephone;
    private String emailAddress;


}
