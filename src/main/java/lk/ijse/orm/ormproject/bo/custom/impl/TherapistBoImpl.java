package lk.ijse.orm.ormproject.bo.custom.impl;

import lk.ijse.orm.ormproject.bo.custom.ProgrammeBo;
import lk.ijse.orm.ormproject.bo.custom.TherapistBo;
import lk.ijse.orm.ormproject.dao.DaoFactory;
import lk.ijse.orm.ormproject.dao.DaoTypes;
import lk.ijse.orm.ormproject.dao.custom.ProgrammeDao;
import lk.ijse.orm.ormproject.dao.custom.TherapistDao;
import lk.ijse.orm.ormproject.dto.TherapistDto;
import lk.ijse.orm.ormproject.entity.Programme;
import lk.ijse.orm.ormproject.entity.Therapist;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class TherapistBoImpl implements TherapistBo {

    TherapistDao therapistDao = DaoFactory.getInstance().getDao(DaoTypes.THERAPIST);
    ProgrammeDao programmeDao = DaoFactory.getInstance().getDao(DaoTypes.PROGRAMME);

    @Override
    public String generateNewTherapistID() throws Exception {
        Optional<String> lastPk = therapistDao.lastPK();
        if (lastPk.isPresent()) {
            String subString = lastPk.get().substring(1);
            int id = Integer.parseInt(subString);
            int newId = id + 1;
            return String.format("T%03d", newId);
        }
        return "T001";
    }

    @Override
    public List<TherapistDto> getAllTherapist() throws Exception {
        List<TherapistDto> therapistDtos = new ArrayList<>();
        List<Therapist> all = therapistDao.getAll();
        for (Therapist therapist : all) {
            TherapistDto therapistDto = new TherapistDto();

            therapistDto.setId(therapist.getId());
            therapistDto.setName(therapist.getName());
            therapistDto.setProgramme(therapist.getProgramme().getId());
            therapistDto.setEmail(therapist.getEmail());
            therapistDto.setTelephone(therapist.getTelephone());

            therapistDtos.add(therapistDto);
        }
        return therapistDtos;
    }

    @Override
    public boolean saveTherapist(TherapistDto therapistDto) throws Exception {
        Therapist therapist = new Therapist();

        therapist.setId(therapistDto.getId());
        therapist.setName(therapistDto.getName());
        therapist.setEmail(therapistDto.getEmail());
        therapist.setTelephone(therapistDto.getTelephone());

        Optional<Programme> programmeByName = programmeDao.findProgrammeByName(therapistDto.getProgramme());
        if (programmeByName.isPresent()) {
            therapist.setProgramme(programmeByName.get());
        }

        return therapistDao.save(therapist);
    }


    @Override
    public boolean deleteTherapist(String id) throws Exception {
        return therapistDao.delete(id);
    }

    @Override
    public boolean updateTherapist(TherapistDto therapistDto) throws Exception {
        Therapist therapist = new Therapist();

        therapist.setId(therapistDto.getId());
        therapist.setName(therapistDto.getName());
        therapist.setEmail(therapistDto.getEmail());
        therapist.setTelephone(therapistDto.getTelephone());
        Optional<Programme> programmeByName = programmeDao.findProgrammeByName(therapistDto.getProgramme());
        if (programmeByName.isPresent()) {
            therapist.setProgramme(programmeByName.get());
        }

        return therapistDao.update(therapist);
    }

    @Override
    public List<String> getAllProgrammeNamesForAssignTherapists() throws Exception {
        Optional<List<String>> allProgrammeNames = programmeDao.getAllProgrammeNames();
        if (allProgrammeNames.isPresent()) {
            return allProgrammeNames.get();
        }
        return null;
    }
}
