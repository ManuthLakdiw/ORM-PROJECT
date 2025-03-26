package lk.ijse.orm.ormproject.dao.custom.impl;

import lk.ijse.orm.ormproject.config.FactoryConfiguration;
import lk.ijse.orm.ormproject.dao.custom.UserDao;
import lk.ijse.orm.ormproject.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


import java.util.List;
import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class UserDaoImpl implements UserDao {
    @Override
    public boolean save(User entity) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();

        try {
            session.persist(entity);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(User entity) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();

        try {
            User user = session.get(User.class, entity.getId());
            user.setUsername(entity.getUsername());
            Optional<String> password = Optional.ofNullable(entity.getPassword());
            if (password.isPresent()) {
                user.setPassword(entity.getPassword());
            }
            user.setRole(entity.getRole());
            session.merge(user);
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
            User user = session.get(User.class, id);
            session.remove(user);
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
    public List<User> getAll() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<User> users = session.createQuery("from User", User.class).list();
        session.close();
        return users;
    }

    @Override
    public Optional<User> findById(String pk) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        User user = session.find(User.class, pk);
        return Optional.ofNullable(user);

    }


    @Override
    public Optional<String> lastPK() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        String lastPK = session.createQuery("SELECT u.id from User u order by u.id desc", String.class).setMaxResults(1).uniqueResult();
        session.close();
        return Optional.ofNullable(lastPK);
    }

    @Override
    public Optional<User> getUserByUserName(String username) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        User user = null;

        try {
            Query<User> qUser = session.createQuery("FROM User WHERE username = :username", User.class);
            qUser.setParameter("username", username);

            user = qUser.getSingleResult();
            System.out.println(user.getId());
            System.out.println(user.getUsername());
            System.out.println(user.getPassword());
            System.out.println(user.getRole());
            tx.commit();
            System.out.println("have user");

        } catch (Exception e) {
            System.out.println("dont have user");

        } finally {
            session.close();
        }

        return Optional.ofNullable(user);
    }
}
