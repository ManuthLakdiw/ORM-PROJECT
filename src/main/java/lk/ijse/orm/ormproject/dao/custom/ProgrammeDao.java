package lk.ijse.orm.ormproject.dao.custom;

import lk.ijse.orm.ormproject.dao.CrudDao;
import lk.ijse.orm.ormproject.entity.Programme;

import java.util.List;
import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public interface ProgrammeDao extends CrudDao<Programme, String> {
   Optional<List<String>> getAllProgrammeNames() throws Exception;
   Optional<Programme> findProgrammeByName(String programmeName) throws Exception;

}
