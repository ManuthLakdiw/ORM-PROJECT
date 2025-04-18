package lk.ijse.orm.ormproject.dao.custom.impl;

import lk.ijse.orm.ormproject.dao.custom.TherapistDao;
import lk.ijse.orm.ormproject.entity.Therapist;

import java.util.List;
import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class TherapistDaoImpl implements TherapistDao {

    @Override
    public boolean save(Therapist entity) throws Exception {
        return false;
    }

    @Override
    public boolean update(Therapist entity) throws Exception {
        return false;
    }

    @Override
    public boolean delete(String s) throws Exception {
        return false;
    }

    @Override
    public List<Therapist> getAll() throws Exception {
        return List.of();
    }

    @Override
    public Optional<Therapist> findById(String pk) throws Exception {
        return Optional.empty();
    }

    @Override
    public Optional<String> lastPK() throws Exception {
        return Optional.empty();
    }
}
