package lk.ijse.orm.ormproject.bo.custom;

import lk.ijse.orm.ormproject.bo.SuperBo;
import lk.ijse.orm.ormproject.dto.PatientDto;
import lk.ijse.orm.ormproject.dto.TherapistDto;
import lk.ijse.orm.ormproject.entity.Therapist;

import java.util.List;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public interface TherapistBo extends SuperBo {
    String generateNewTherapistID() throws Exception;
    List<TherapistDto> getAllTherapist() throws Exception;
    boolean saveTherapist(TherapistDto therapistDto) throws Exception;
    boolean deleteTherapist(String id) throws Exception;
    boolean updateTherapist(TherapistDto therapistDto) throws Exception;
    List<String> getAllProgrammeNamesForAssignTherapists() throws Exception;
}
