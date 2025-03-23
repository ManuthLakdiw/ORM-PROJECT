package lk.ijse.orm.ormproject.bo.custom;

import lk.ijse.orm.ormproject.bo.SuperBo;
import lk.ijse.orm.ormproject.dto.ProgrammeDto;
import lk.ijse.orm.ormproject.dto.UserDto;
import lk.ijse.orm.ormproject.entity.Programme;

import java.util.List;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public interface ProgrammeBo extends SuperBo {

    boolean saveProgramme(ProgrammeDto programmeDto) throws Exception;
    List<ProgrammeDto> getAllProgrammes() throws Exception;
    String generateNewProgrammeID() throws Exception;
    boolean deleteProgramme(String id) throws Exception;
    boolean updateProgramme(ProgrammeDto programmeDto) throws Exception;




}
