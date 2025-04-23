package lk.ijse.orm.ormproject.dao.custom.impl;

import lk.ijse.orm.ormproject.config.FactoryConfiguration;
import lk.ijse.orm.ormproject.dao.custom.AppointmentDao;
import lk.ijse.orm.ormproject.entity.Appointment;
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
public class AppointmentDaoImpl implements AppointmentDao {

    @Override
    public boolean save(Appointment entity) throws Exception {
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
    public boolean update(Appointment entity) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();

        try {
            Appointment appointment = session.get(Appointment.class, entity.getId());
            appointment.setPatient(entity.getPatient());
            appointment.setTherapySession(entity.getTherapySession());
            appointment.setTime(entity.getTime());
            appointment.setDate(entity.getDate());

            session.merge(appointment);
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
            Appointment appointment = session.get(Appointment.class, id);
            session.remove(appointment);
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
    public List<Appointment> getAll() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<Appointment> appointments = session.createQuery("from Appointment", Appointment.class).list();
        return appointments;
    }

    @Override
    public Optional<Appointment> findById(String pk) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Appointment appointment = session.get(Appointment.class, pk);
        return Optional.ofNullable(appointment);
    }

    @Override
    public Optional<String> lastPK() throws Exception {
        Session session  = FactoryConfiguration.getInstance().getSession();
        String lastPK = session.createQuery("SELECT a.id from Appointment a order by a.id desc", String.class).setMaxResults(1).uniqueResult();
        return Optional.ofNullable(lastPK);
    }


    @Override
    public Optional<Integer> getAppointmentCountBySession(String session) throws Exception {
        Session sn = FactoryConfiguration.getInstance().getSession();
        try {
            Integer count = sn.createNativeQuery(
                            "SELECT COUNT(*) FROM appointment_table WHERE therapySession_id = :session AND date >= CURRENT_DATE", Integer.class)
                    .setParameter("session", session)
                    .uniqueResult();

            return Optional.ofNullable(count);
        } finally {
            sn.close();
        }
    }


}
