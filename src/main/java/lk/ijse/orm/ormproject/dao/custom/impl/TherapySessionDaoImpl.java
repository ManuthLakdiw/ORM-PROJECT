package lk.ijse.orm.ormproject.dao.custom.impl;

import lk.ijse.orm.ormproject.config.FactoryConfiguration;
import lk.ijse.orm.ormproject.dao.custom.TherapySessionDao;
import lk.ijse.orm.ormproject.entity.TherapySession;
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
public class TherapySessionDaoImpl implements TherapySessionDao {
    @Override
    public boolean save(TherapySession entity) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();

        try {
            session.persist(entity);
            tx.commit();
            return true;

        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public boolean update(TherapySession entity) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();

        try {
            TherapySession therapySession = session.get(TherapySession.class, entity.getId());
            therapySession.setProgramme(entity.getProgramme());
            therapySession.setTherapist(entity.getTherapist());
            therapySession.setDate(entity.getDate());
            therapySession.setStartTime(entity.getStartTime());
            therapySession.setEndTime(entity.getEndTime());

            session.merge(therapySession);
            tx.commit();
            return true;
        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
            return false;
        }finally {
            session.close();
        }

    }

    @Override
    public boolean delete(String id) throws Exception {
       Session session = FactoryConfiguration.getInstance().getSession();
       Transaction tx = session.beginTransaction();

       try {
           TherapySession therapySession = session.get(TherapySession.class, id);
           session.remove(therapySession);
           tx.commit();
           return true;
       }catch (Exception e){
           tx.rollback();
           e.printStackTrace();
           return false;
       }finally {
           session.close();
       }
    }

    @Override
    public List<TherapySession> getAll() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<TherapySession> therapySessions = session.createQuery("from TherapySession").list();
        session.close();
        return therapySessions;
    }

    @Override
    public Optional<TherapySession> findById(String pk) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        TherapySession therapySession = session.find(TherapySession.class, pk);
        session.close();
        return Optional.ofNullable(therapySession);
    }

    @Override
    public Optional<String> lastPK() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        String lastPK = session.createQuery("SELECT ts.id from TherapySession ts order by ts.id desc", String.class).setMaxResults(1).uniqueResult();
        session.close();
        return Optional.ofNullable(lastPK);
    }
}
