package lk.ijse.orm.ormproject.bo.custom;

import lk.ijse.orm.ormproject.bo.SuperBo;
import lk.ijse.orm.ormproject.dto.PatientDto;
import lk.ijse.orm.ormproject.dto.UserDto;
import lk.ijse.orm.ormproject.entity.Patient;

import java.util.List;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public interface PatientBo extends SuperBo {
    String generateNewPatientID() throws Exception;
    List<PatientDto> getAllPatients() throws Exception;
    boolean savePatient(PatientDto patientDto) throws Exception;
    boolean deletePatient(String id) throws Exception;
    boolean updatePatient(PatientDto patientDto) throws Exception;
    PatientDto getPatient(String id) throws Exception;
}
