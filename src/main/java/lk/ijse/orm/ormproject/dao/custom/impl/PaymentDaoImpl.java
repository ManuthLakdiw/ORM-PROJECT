package lk.ijse.orm.ormproject.dao.custom.impl;

import lk.ijse.orm.ormproject.config.FactoryConfiguration;
import lk.ijse.orm.ormproject.dao.custom.PaymentDao;
import lk.ijse.orm.ormproject.entity.Payment;
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
public class PaymentDaoImpl implements PaymentDao {
    @Override
    public boolean save(Payment entity) throws Exception {
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
    public boolean update(Payment entity) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();

        try {
            Payment payment = session.get(Payment.class, entity.getId());
            payment.setAppointment(entity.getAppointment());
            payment.setSessionFee(entity.getSessionFee());
            payment.setPaidAmount(entity.getPaidAmount());
            payment.setDueAmount(entity.getDueAmount());
            payment.setStatus(entity.getStatus());
            payment.setDate(entity.getDate());

            session.merge(payment);
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
            Payment payment = session.get(Payment.class, id);
            session.remove(payment);
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
    public List<Payment> getAll() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();

        List<Payment> payments = session.createQuery("from Payment").list();
        session.close();
        return payments;

    }

    @Override
    public Optional<Payment> findById(String pk) throws Exception {

        Session session = FactoryConfiguration.getInstance().getSession();
        Payment payment = session.get(Payment.class, pk);
        session.close();
        return Optional.ofNullable(payment);
    }

    @Override
    public Optional<String> lastPK() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        String lastPK = session.createQuery("SELECT pay.id from Payment pay order by pay.id desc", String.class).setMaxResults(1).uniqueResult();
        session.close();
        return Optional.ofNullable(lastPK);
    }
}
