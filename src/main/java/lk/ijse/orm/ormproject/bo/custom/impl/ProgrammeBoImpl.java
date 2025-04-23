package lk.ijse.orm.ormproject.bo.custom.impl;

import lk.ijse.orm.ormproject.bo.custom.ProgrammeBo;
import lk.ijse.orm.ormproject.dao.DaoFactory;
import lk.ijse.orm.ormproject.dao.DaoTypes;
import lk.ijse.orm.ormproject.dao.custom.ProgrammeDao;
import lk.ijse.orm.ormproject.dto.ProgrammeDto;
import lk.ijse.orm.ormproject.entity.Programme;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class ProgrammeBoImpl implements ProgrammeBo {

    ProgrammeDao programmeDao = DaoFactory.getInstance().getDao(DaoTypes.PROGRAMME);

    @Override
    public boolean saveProgramme(ProgrammeDto programmeDto) throws Exception {

        Programme programme = new Programme();
        programme.setId(programmeDto.getId());
        programme.setProgrammeName(programmeDto.getProgrammeName());
        programme.setDuration(programmeDto.getDuration());
        programme.setFee(programmeDto.getFee());

        return programmeDao.save(programme);
    }

    @Override
    public List<ProgrammeDto> getAllProgrammes() throws Exception {
        List<ProgrammeDto> programmeDtos = new ArrayList<>();
        List<Programme> programmeEntityList = programmeDao.getAll();

        for (Programme programmeEntity : programmeEntityList) {
            ProgrammeDto programmeDto = new ProgrammeDto();

            programmeDto.setId(programmeEntity.getId());
            programmeDto.setProgrammeName(programmeEntity.getProgrammeName());
            programmeDto.setDuration(programmeEntity.getDuration());
            programmeDto.setFee(programmeEntity.getFee());
            programmeDtos.add(programmeDto);
        }
        return programmeDtos;
    }

    @Override
    public String generateNewProgrammeID() throws Exception {
        Optional<String> lastID = programmeDao.lastPK();
        if (lastID.isPresent()) {
            String substring = lastID.get().substring(1);
            int number = Integer.parseInt(substring);
            int newId = number + 1;
            return String.format("P%03d", newId);
        }
        return "P001";
    }

    @Override
    public boolean deleteProgramme(String id) throws Exception {
        return programmeDao.delete(id);
    }

    @Override
    public boolean updateProgramme(ProgrammeDto programmeDto) throws Exception {
        Programme programme = new Programme();
        programme.setProgrammeName(programmeDto.getProgrammeName());
        programme.setId(programmeDto.getId());
        programme.setDuration(programmeDto.getDuration());
        programme.setFee(programmeDto.getFee());
        return programmeDao.update(programme);
    }

    @Override
    public ProgrammeDto getProgramme(String id) throws Exception {
        Optional<Programme> byId = programmeDao.findById(id);
        if (byId.isPresent()) {
            ProgrammeDto programmeDto = new ProgrammeDto();
            programmeDto.setId(byId.get().getId());
            programmeDto.setProgrammeName(byId.get().getProgrammeName());
            programmeDto.setDuration(byId.get().getDuration());
            programmeDto.setFee(byId.get().getFee());
            return programmeDto;
        }
        return null;
    }
}
