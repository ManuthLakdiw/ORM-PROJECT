package lk.ijse.orm.ormproject.entity;

import jakarta.persistence.*;
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

@Entity
@Table(name = "therapist_table")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Therapist implements SuperEntity {

    @Id
    private String id;
    private String name;
    private String email;

    @ManyToOne
    private Programme programme;

}


