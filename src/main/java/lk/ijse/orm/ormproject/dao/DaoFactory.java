package lk.ijse.orm.ormproject.dao;

import lk.ijse.orm.ormproject.dao.custom.impl.*;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class DaoFactory {
    private static DaoFactory daoFactory;
    private DaoFactory() {}

    public static DaoFactory getInstance() {
        return daoFactory == null ? daoFactory = new DaoFactory() : daoFactory;
    }

    @SuppressWarnings("unchecked")
    public <T extends SuperDao> T getDao(DaoTypes daoType) {
        return switch (daoType){
            case USER -> (T) new UserDaoImpl();
            case PROGRAMME -> (T) new ProgrammeDaoImpl();
            case PATIENT -> (T) new PatientDaoImpl();
            case THERAPIST -> (T) new TherapistDaoImpl();
            case THERAPYSESSION -> (T) new TherapySessionDaoImpl();
            case APPOINTMENT -> (T) new AppointmentDaoImpl();
            case PAYMENT -> (T) new PaymentDaoImpl();
        };
    }
}
