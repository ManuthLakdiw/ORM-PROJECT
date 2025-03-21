package lk.ijse.orm.ormproject.dao.custom.impl;

import lk.ijse.orm.ormproject.dao.custom.ProgrammeDao;
import lk.ijse.orm.ormproject.entity.Programme;

import java.util.List;
import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class ProgrammeDaoImpl implements ProgrammeDao {
    @Override
    public boolean save(Programme entity) throws Exception {
        return false;
    }

    @Override
    public boolean update(Programme entity) throws Exception {
        return false;
    }

    @Override
    public boolean delete(String s) throws Exception {
        return false;
    }

    @Override
    public List<Programme> getAll() throws Exception {
        return List.of();
    }

    @Override
    public Optional<Programme> findById(String pk) throws Exception {
        return Optional.empty();
    }

    @Override
    public Optional<String> lastPK() throws Exception {
        return Optional.empty();
    }
}
