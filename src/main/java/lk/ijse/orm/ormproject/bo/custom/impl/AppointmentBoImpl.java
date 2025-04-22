package lk.ijse.orm.ormproject.bo.custom.impl;

import lk.ijse.orm.ormproject.bo.custom.AppointmentBo;
import lk.ijse.orm.ormproject.dao.DaoFactory;
import lk.ijse.orm.ormproject.dao.DaoTypes;
import lk.ijse.orm.ormproject.dao.custom.*;
import lk.ijse.orm.ormproject.dto.AppointmentDto;
import lk.ijse.orm.ormproject.dto.PatientDto;
import lk.ijse.orm.ormproject.dto.TherapySessionDto;
import lk.ijse.orm.ormproject.entity.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class AppointmentBoImpl implements AppointmentBo {

    AppointmentDao appointmentDao = DaoFactory.getInstance().getDao(DaoTypes.APPOINTMENT);
    PatientDao patientDao = DaoFactory.getInstance().getDao(DaoTypes.PATIENT);
    TherapySessionDao therapySessionDao = DaoFactory.getInstance().getDao(DaoTypes.THERAPYSESSION);
    TherapistDao therapistDao = DaoFactory.getInstance().getDao(DaoTypes.THERAPIST);



    @Override
    public String generateNewAppointmentID() throws Exception {
        Optional<String> lastPK = appointmentDao.lastPK();
        if (lastPK.isPresent()) {
            String subString = lastPK.get().substring(1);
            int id = Integer.parseInt(subString);
            int newId = id + 1;
            return String.format("A%03d", newId);
        }
        return "A001";
    }

    @Override
    public List<AppointmentDto> getAllAppointment() throws Exception {
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        List<Appointment> all = appointmentDao.getAll();
        for (Appointment appointment : all) {
            AppointmentDto appointmentDto = new AppointmentDto();

            appointmentDto.setId(appointment.getId());
            appointmentDto.setPatient(appointment.getPatient().getId());
            appointmentDto.setSession(appointment.getTherapySession().getId());
            appointmentDto.setTime(appointment.getTime());
            appointmentDtos.add(appointmentDto);
        }
        return appointmentDtos;
    }

    @Override
    public boolean saveAppointment(AppointmentDto appointmentDto) throws Exception {
        Appointment appointment = new Appointment();

        appointment.setId(appointmentDto.getId());
        Optional<Patient> patient = patientDao.findById(appointmentDto.getPatient());
        if (patient.isPresent()) {
            appointment.setPatient(patient.get());
        }
        Optional<TherapySession> therapySession = therapySessionDao.findById(appointmentDto.getSession());
        if (therapySession.isPresent()) {
            appointment.setTherapySession(therapySession.get());
        }
        appointment.setTime(appointmentDto.getTime());
        appointment.setDate(appointmentDto.getDate());

        return appointmentDao.save(appointment);

    }

    @Override
    public boolean deleteAppointment(String id) throws Exception {
       return appointmentDao.delete(id);
    }

    @Override
    public boolean updateAppointment(AppointmentDto appointmentDto) throws Exception {
        Appointment appointment = new Appointment();

        appointment.setId(appointmentDto.getId());
        appointment.setTime(appointmentDto.getTime());
        Optional<Patient> byId = patientDao.findById(appointmentDto.getPatient());
        if (byId.isPresent()) {
            appointment.setPatient(byId.get());
        }
        Optional<TherapySession> therapySession = therapySessionDao.findById(appointmentDto.getSession());
        if (therapySession.isPresent()) {
            appointment.setTherapySession(therapySession.get());
        }
        appointment.setDate(appointmentDto.getDate());

        return appointmentDao.update(appointment);

    }

    @Override
    public List<PatientDto> getAllPatientsForAppointment() throws Exception {
        List<Patient> all = patientDao.getAll();
        List<PatientDto> patientDtos = new ArrayList<>();
        for (Patient patient : all) {
            PatientDto patientDto = new PatientDto();
            patientDto.setId(patient.getId());
            patientDto.setName(patient.getName());
            patientDtos.add(patientDto);
        }
        return patientDtos;
    }

    @Override
    public List<TherapySessionDto> getAllSessionsForAppointment() throws Exception {
        List<TherapySession> all = therapySessionDao.getAll();
        List<TherapySessionDto> therapySessionDtos = new ArrayList<>();
        for (TherapySession therapySession : all) {
            TherapySessionDto therapySessionDto = new TherapySessionDto();
            therapySessionDto.setId(therapySession.getId());

            Optional<Therapist> byId = therapistDao.findById(therapySession.getTherapist());
            if (byId.isPresent()) {
                therapySessionDto.setTherapist(byId.get().getName());
            }

            therapySessionDto.setProgramme(therapySession.getProgramme().getProgrammeName());
            therapySessionDto.setDate(therapySession.getDate());
            therapySessionDto.setStartTime(therapySession.getStartTime());
            therapySessionDto.setEndTime(therapySession.getEndTime());
            therapySessionDtos.add(therapySessionDto);

        }
        return therapySessionDtos;
    }

}
