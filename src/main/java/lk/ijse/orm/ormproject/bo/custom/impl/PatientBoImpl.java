package lk.ijse.orm.ormproject.bo.custom.impl;

import lk.ijse.orm.ormproject.bo.custom.PatientBo;
import lk.ijse.orm.ormproject.dao.DaoFactory;
import lk.ijse.orm.ormproject.dao.DaoTypes;
import lk.ijse.orm.ormproject.dao.custom.PatientDao;
import lk.ijse.orm.ormproject.dto.PatientDto;
import lk.ijse.orm.ormproject.entity.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class PatientBoImpl implements PatientBo {

    PatientDao patientDao = DaoFactory.getInstance().getDao(DaoTypes.PATIENT);
    @Override
    public String generateNewPatientID() throws Exception {
        Optional<String> lastId = patientDao.lastPK();
        if (lastId.isPresent()) {
            String substring = lastId.get().substring(1);
            int number = Integer.parseInt(substring);
            int newId = number + 1;
            return String.format("P%03d", newId);

        }
        return "P001";
    }

    @Override
    public List<PatientDto> getAllPatients() throws Exception {
        List<PatientDto> patientDtos = new ArrayList<>();
        List<Patient> patients = patientDao.getAll();
        for (Patient patient : patients) {
            PatientDto patientDto = new PatientDto();

            patientDto.setId(patient.getId());
            patientDto.setTitle(patient.getTitle());
            patientDto.setName(patient.getName());
            patientDto.setDob(patient.getDob());
            patientDto.setHomeAddress(patient.getHomeAddress());
            patientDto.setTelephone(patient.getTelephone());
            patientDto.setEmailAddress(patient.getEmailAddress());
            System.out.println("email is : "+ patient.getEmailAddress());

            patientDtos.add(patientDto);
        }
        return patientDtos;
    }

    @Override
    public boolean savePatient(PatientDto patientDto) throws Exception {
       Patient patient = new Patient();
       patient.setId(patientDto.getId());
       patient.setTitle(patientDto.getTitle());
       patient.setName(patientDto.getName().trim());
       patient.setDob(patientDto.getDob());
       patient.setHomeAddress(patientDto.getHomeAddress().trim());
       patient.setTelephone(patientDto.getTelephone().trim());
       patient.setEmailAddress(patientDto.getEmailAddress().trim());

       return patientDao.save(patient);

    }

    @Override
    public boolean deletePatient(String id) throws Exception {
        return patientDao.delete(id);
    }

    @Override
    public boolean updatePatient(PatientDto patientDto) throws Exception {
        Patient patient = new Patient();
        patient.setId(patientDto.getId());
        patient.setTitle(patientDto.getTitle());
        patient.setName(patientDto.getName().trim());
        patient.setDob(patientDto.getDob());
        patient.setHomeAddress(patientDto.getHomeAddress().trim());
        patient.setTelephone(patientDto.getTelephone().trim());
        patient.setEmailAddress(patientDto.getEmailAddress().trim());

        return patientDao.update(patient);

    }
}
