package lk.ijse.orm.ormproject.dao.custom;

import lk.ijse.orm.ormproject.dao.CrudDao;
import lk.ijse.orm.ormproject.entity.Therapist;

import java.util.List;
import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public interface TherapistDao extends CrudDao<Therapist, String> {
    Optional<Therapist> getTherapistByName(String therapistName) throws Exception;
    List<Therapist> getTherapistsByProgrammeID(String programId) throws Exception;
}
