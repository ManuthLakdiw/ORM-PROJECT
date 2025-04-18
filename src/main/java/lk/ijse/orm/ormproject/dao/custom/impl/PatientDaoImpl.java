package lk.ijse.orm.ormproject.dao.custom.impl;

import lk.ijse.orm.ormproject.config.FactoryConfiguration;
import lk.ijse.orm.ormproject.dao.custom.PatientDao;
import lk.ijse.orm.ormproject.entity.Patient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class PatientDaoImpl implements PatientDao {
    @Override
    public boolean save(Patient entity) throws Exception {
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
    public boolean update(Patient entity) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();

        try {
            Patient patient = session.get(Patient.class, entity.getId());
            patient.setTitle(entity.getTitle());
            patient.setName(entity.getName());
            patient.setDob(entity.getDob());
            patient.setHomeAddress(entity.getHomeAddress());
            patient.setTelephone(entity.getTelephone());
            patient.setEmailAddress(entity.getEmailAddress());
            session.merge(patient);
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
            Patient patient = session.get(Patient.class, id);
            session.remove(patient);
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
    public List<Patient> getAll() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<Patient> patientList = session.createQuery("from Patient", Patient.class).list();
        return patientList;

    }

    @Override
    public Optional<Patient> findById(String pk) throws Exception {
       Session session = FactoryConfiguration.getInstance().getSession();

       Patient patient = session.find(Patient.class, pk);
       return Optional.ofNullable(patient);
    }

    @Override
    public Optional<String> lastPK() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        String lastPK = session.createQuery("SELECT p.id from Patient p order by p.id desc", String.class).setMaxResults(1).uniqueResult();
        return Optional.ofNullable(lastPK);
    }
}
