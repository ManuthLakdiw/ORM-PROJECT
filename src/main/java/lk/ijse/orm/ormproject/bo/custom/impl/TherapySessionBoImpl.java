package lk.ijse.orm.ormproject.bo.custom.impl;

import lk.ijse.orm.ormproject.bo.custom.TherapySessionBo;
import lk.ijse.orm.ormproject.dao.DaoFactory;
import lk.ijse.orm.ormproject.dao.DaoTypes;
import lk.ijse.orm.ormproject.dao.custom.ProgrammeDao;
import lk.ijse.orm.ormproject.dao.custom.TherapistDao;
import lk.ijse.orm.ormproject.dao.custom.TherapySessionDao;
import lk.ijse.orm.ormproject.dto.TherapistDto;
import lk.ijse.orm.ormproject.dto.TherapySessionDto;
import lk.ijse.orm.ormproject.entity.Programme;
import lk.ijse.orm.ormproject.entity.Therapist;
import lk.ijse.orm.ormproject.entity.TherapySession;

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
    public List<TherapistDto> getAllTherapySessions() throws Exception {
        return List.of();
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
        return false;
    }

    @Override
    public boolean updateTherapySession(TherapySessionDto therapySessionDto) throws Exception {
        return false;
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
        for (Therapist therapist : therapistsByProgrammeName) {
            therapistNames.add(therapist.getName());
        }
        return therapistNames;
    }
}
