package lk.ijse.orm.ormproject.config;

import lk.ijse.orm.ormproject.entity.Patient;
import lk.ijse.orm.ormproject.entity.Programme;
import lk.ijse.orm.ormproject.entity.Therapist;
import lk.ijse.orm.ormproject.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.management.relation.Role;
import java.io.IOException;
import java.util.Properties;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github <a href="https://github.com/ManuthLakdiw">...</a>
 */

public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;
    private final SessionFactory sessionFactory;
    private FactoryConfiguration() {
        Configuration cfg = new Configuration();
        Properties properties = new Properties();

        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("hibernate.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load hibernate.properties");
        }

        cfg.setProperties(properties);
        cfg.addAnnotatedClass(User.class);
        cfg.addAnnotatedClass(Programme.class);
        cfg.addAnnotatedClass(Therapist.class);
        cfg.addAnnotatedClass(Patient.class);
        sessionFactory = cfg.buildSessionFactory();
    }

    public static FactoryConfiguration getInstance() {
        if (factoryConfiguration == null) {
            factoryConfiguration = new FactoryConfiguration();
        }
        return factoryConfiguration;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}
