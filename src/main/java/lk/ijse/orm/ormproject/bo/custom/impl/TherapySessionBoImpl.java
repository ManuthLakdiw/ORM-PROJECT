package lk.ijse.orm.ormproject.bo.custom.impl;

import lk.ijse.orm.ormproject.bo.custom.TherapySessionBo;
import lk.ijse.orm.ormproject.dao.DaoFactory;
import lk.ijse.orm.ormproject.dao.DaoTypes;
import lk.ijse.orm.ormproject.dao.custom.ProgrammeDao;
import lk.ijse.orm.ormproject.dao.custom.TherapistDao;
import lk.ijse.orm.ormproject.dao.custom.TherapySessionDao;
import lk.ijse.orm.ormproject.dto.TherapySessionDto;
import lk.ijse.orm.ormproject.entity.Programme;
import lk.ijse.orm.ormproject.entity.Therapist;
import lk.ijse.orm.ormproject.entity.TherapySession;
import lk.ijse.orm.ormproject.exception.ScheduleConflictException;

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
public class TherapySessionBoImpl implements TherapySessionBo {
    TherapySessionDao therapySessionDao = DaoFactory.getInstance().getDao(DaoTypes.THERAPYSESSION);
    ProgrammeDao programmeDao = DaoFactory.getInstance().getDao(DaoTypes.PROGRAMME);
    TherapistDao therapistDao = DaoFactory.getInstance().getDao(DaoTypes.THERAPIST);

    @Override
    public String generateNewTherapySessionID() throws Exception {
        Optional<String> lastId = therapySessionDao.lastPK();
        if (lastId.isPresent()) {
            String subString = lastId.get().substring(1);
            int id = Integer.parseInt(subString);
            int newId = id + 1;
            return String.format("S%03d", newId);
        }
        return "S001";
    }

    @Override
    public List<TherapySessionDto> getAllTherapySessions() throws Exception {
       List<TherapySessionDto> therapySessions = new ArrayList<>();
        List<TherapySession> all = therapySessionDao.getAll();
        for (TherapySession therapySession : all) {
            TherapySessionDto therapySessionDto = new TherapySessionDto();
            therapySessionDto.setId(therapySession.getId());
            therapySessionDto.setProgramme(therapySession.getProgramme().getProgrammeName());
            Optional<Therapist> byId = therapistDao.findById(therapySession.getTherapist());
            if (byId.isPresent()) {
                therapySessionDto.setTherapist(byId.get().getName());
            }
            therapySessionDto.setDate(therapySession.getDate());
            therapySessionDto.setStartTime(therapySession.getStartTime());
            therapySessionDto.setEndTime(therapySession.getEndTime());
            therapySessions.add(therapySessionDto);
        }
        return therapySessions;
    }

    @Override
    public boolean saveTherapySession(TherapySessionDto therapySessionDto) throws Exception {
        TherapySession therapySession = new TherapySession();

        therapySession.setId(therapySessionDto.getId());
        therapySession.setDate(therapySessionDto.getDate());
        therapySession.setStartTime(therapySessionDto.getStartTime());
        therapySession.setEndTime(therapySessionDto.getEndTime());
        Optional<Programme> programmeByName = programmeDao.findProgrammeByName(therapySessionDto.getProgramme());
        if (programmeByName.isPresent()) {
            therapySession.setProgramme(programmeByName.get());
        }
        Optional<Therapist> therapistByName = therapistDao.getTherapistByName(therapySessionDto.getTherapist());
        if (therapistByName.isPresent()) {
            therapySession.setTherapist(therapistByName.get().getId());
        }

        return therapySessionDao.save(therapySession);
    }

    @Override
    public boolean deleteTherapySession(String id) throws Exception {
        return therapySessionDao.delete(id);
    }

    @Override
    public boolean updateTherapySession(TherapySessionDto therapySessionDto) throws Exception {
        TherapySession therapySession = new TherapySession();

        therapySession.setId(therapySessionDto.getId());
        therapySession.setDate(therapySessionDto.getDate());
        therapySession.setStartTime(therapySessionDto.getStartTime());
        therapySession.setEndTime(therapySessionDto.getEndTime());
        Optional<Therapist> therapistByName = therapistDao.getTherapistByName(therapySessionDto.getTherapist());
        if (therapistByName.isPresent()) {
            therapySession.setTherapist(therapistByName.get().getId());
        }
        Optional<Programme> programmeByName = programmeDao.findProgrammeByName(therapySessionDto.getProgramme());
        if (programmeByName.isPresent()) {
            therapySession.setProgramme(programmeByName.get());
        }

        return therapySessionDao.update(therapySession);

    }

    @Override
    public Optional<List<String>> getAllProgrammeNamesForScheduleSession() throws Exception {
        Optional<List<String>> allProgrammeNames = programmeDao.getAllProgrammeNames();
        if (allProgrammeNames.isPresent()) {
            return allProgrammeNames;
        }
        return Optional.empty();
    }

    @Override
    public List<String> getAllTherapistNamesByTherapyProgram(String programmeName) throws Exception {
        List<String> therapistNames = new ArrayList<>();
        List<Therapist> therapistsByProgrammeName = null;
        Optional<Programme> programmeByName = programmeDao.findProgrammeByName(programmeName);
        if (programmeByName.isPresent()) {
            therapistsByProgrammeName = therapistDao.getTherapistsByProgrammeID(programmeByName.get().getId());
        }
        if (therapistsByProgrammeName != null) {
            for (Therapist therapist : therapistsByProgrammeName) {
                therapistNames.add(therapist.getName());
            }
        }
        return therapistNames;
    }

    @Override
    public boolean iScheduleConflict(String id,LocalDate scheduleDate, String program, LocalTime newStartTime, LocalTime newEndTime, String therapist) throws Exception {
        List<TherapySession> allSessions = therapySessionDao.getAll();
        String therapistID = null;
        Optional<Therapist> therapistByName = therapistDao.getTherapistByName(therapist);
        if (therapistByName.isPresent()) {
            therapistID = therapistByName.get().getId();
        }
        for (TherapySession session : allSessions) {
            if (session.getId().equals(id)) {
                continue;
            }
            if (
                    session.getDate().isEqual(scheduleDate) &&
                            session.getProgramme().getProgrammeName().equals(program) &&
                            session.getTherapist().equals(therapistID)
            ) {
                LocalTime existingStart = session.getStartTime();
                LocalTime existingEnd = session.getEndTime();

                boolean isOverlapping = newStartTime.isBefore(existingEnd) && newEndTime.isAfter(existingStart);


                if (isOverlapping) {
                    throw new ScheduleConflictException(
                            "Schedule conflict detected on " + scheduleDate + " between " + existingStart + " and " + existingEnd
                    );
                }
            }
        }
        return false;
    }

    @Override
    public TherapySessionDto getExitingSession(String id) throws Exception {
        TherapySessionDto therapySessionDto = new TherapySessionDto();
        Optional<TherapySession> byId = therapySessionDao.findById(id);
        if (byId.isPresent()) {
            therapySessionDto.setId(byId.get().getId());
            therapySessionDto.setProgramme(byId.get().getProgramme().getProgrammeName());
            Optional<Therapist> therapist = therapistDao.findById(byId.get().getTherapist());
            if (therapist.isPresent()) {
                therapySessionDto.setTherapist(therapist.get().getName());
            }
            therapySessionDto.setStartTime(byId.get().getStartTime());
            therapySessionDto.setEndTime(byId.get().getEndTime());
            therapySessionDto.setDate(byId.get().getDate());
            return therapySessionDto;
        }
        return null;
    }


}
