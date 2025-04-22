package lk.ijse.orm.ormproject.bo.custom;

import lk.ijse.orm.ormproject.bo.SuperBo;
import lk.ijse.orm.ormproject.dto.AppointmentDto;
import lk.ijse.orm.ormproject.dto.PatientDto;
import lk.ijse.orm.ormproject.dto.TherapySessionDto;
import lk.ijse.orm.ormproject.entity.SuperEntity;
import lk.ijse.orm.ormproject.entity.TherapySession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public interface AppointmentBo extends SuperBo {
    String generateNewAppointmentID() throws Exception;
    List<AppointmentDto> getAllAppointment() throws Exception;
    boolean saveAppointment(AppointmentDto appointmentDto) throws Exception;
    boolean deleteAppointment(String id) throws Exception;
    boolean updateAppointment(AppointmentDto appointmentDto) throws Exception;
    List<PatientDto> getAllPatientsForAppointment() throws Exception;
    List<TherapySessionDto> getAllSessionsForAppointment() throws Exception;

}
