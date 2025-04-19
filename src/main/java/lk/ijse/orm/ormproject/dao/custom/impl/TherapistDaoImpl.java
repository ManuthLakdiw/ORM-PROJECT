package lk.ijse.orm.ormproject.dao.custom.impl;

import lk.ijse.orm.ormproject.config.FactoryConfiguration;
import lk.ijse.orm.ormproject.dao.custom.TherapistDao;
import lk.ijse.orm.ormproject.entity.Therapist;
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
public class TherapistDaoImpl implements TherapistDao {

    @Override
    public boolean save(Therapist entity) throws Exception {
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
    public boolean update(Therapist entity) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();

        try {
            Therapist therapist = session.get(Therapist.class, entity.getId());
            therapist.setName(entity.getName());
            therapist.setEmail(entity.getEmail());
            therapist.setProgramme(entity.getProgramme());
            therapist.setTelephone(entity.getTelephone());
            session.merge(therapist);
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
            Therapist therapist = session.get(Therapist.class, id);
            session.remove(therapist);
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
    public List<Therapist> getAll() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<Therapist> therapists = session.createQuery("from Therapist", Therapist.class).list();
        return therapists;
    }

    @Override
    public Optional<Therapist> findById(String pk) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Therapist therapist = session.get(Therapist.class, pk);
        return Optional.ofNullable(therapist);
    }

    @Override
    public Optional<String> lastPK() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        String lastPK = session.createQuery("SELECT t.id from Therapist t order by t.id desc", String.class).setMaxResults(1).uniqueResult();
        return Optional.ofNullable(lastPK);
    }
}
