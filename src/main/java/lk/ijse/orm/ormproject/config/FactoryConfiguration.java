package lk.ijse.orm.ormproject.config;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.Properties;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */


public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;

    SessionFactory sessionFactory;

    private FactoryConfiguration() {
        Configuration cfg = new Configuration();

        Properties properties = new Properties();

        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("hibernate.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        cfg.setProperties(properties);
        cfg.buildSessionFactory();
    }

    public static FactoryConfiguration getInstance() {
        return factoryConfiguration == null ? factoryConfiguration = new FactoryConfiguration() : factoryConfiguration;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }




}
