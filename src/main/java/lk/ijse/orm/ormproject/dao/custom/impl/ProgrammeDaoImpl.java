package lk.ijse.orm.ormproject.dao.custom.impl;

import lk.ijse.orm.ormproject.config.FactoryConfiguration;
import lk.ijse.orm.ormproject.dao.custom.ProgrammeDao;
import lk.ijse.orm.ormproject.entity.Programme;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();

        try {
            session.persist(entity);
            tx.commit();
           return true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return false;
    }

    @Override
    public boolean update(Programme entity) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();

        try {
            Programme programme = session.get(Programme.class, entity.getId());
            programme.setProgrammeName(entity.getProgrammeName());
            programme.setFee(entity.getFee());
            programme.setDuration(entity.getDuration());

            session.merge(programme);
            tx.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return false;
    }

    @Override
    public boolean delete(String id) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();

        try {
            Programme programme = session.get(Programme.class, id);
            session.remove(programme);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }finally {
            session.close();
        }
        return false;
    }

    @Override
    public List<Programme> getAll() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<Programme> programmes = session.createQuery("from Programme", Programme.class).list();
        session.close();
        return programmes;
    }

    @Override
    public Optional<Programme> findById(String pk) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Programme programme = session.get(Programme.class, pk);
        session.close();
        return Optional.ofNullable(programme);
    }

    @Override
    public Optional<String> lastPK() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        String lastPK = session.createQuery("SELECT p.id from Programme p order by p.id desc", String.class).setMaxResults(1).uniqueResult();
        session.close();
        return Optional.ofNullable(lastPK);
    }


    @Override
    public Optional<List<String>> getAllProgrammeNames() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();

        try {
            List<String> programmeNames = session
                    .createQuery("SELECT P.programmeName FROM Programme P", String.class)
                    .getResultList();
            return Optional.of(programmeNames);
        }finally {
            session.close();
        }
    }

    public Optional<Programme> findProgrammeByName(String programmeName) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();

        try {
            Programme programme = session
                    .createQuery("FROM Programme WHERE programmeName = :name", Programme.class)
                    .setParameter("name", programmeName)
                    .uniqueResult();

            return Optional.ofNullable(programme);
        } finally {
            session.close();
        }
    }

}
