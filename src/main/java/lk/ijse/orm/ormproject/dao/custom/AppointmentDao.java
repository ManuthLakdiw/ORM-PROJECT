package lk.ijse.orm.ormproject.dao.custom;

import lk.ijse.orm.ormproject.dao.CrudDao;
import lk.ijse.orm.ormproject.entity.Appointment;

import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public interface AppointmentDao extends CrudDao<Appointment, String> {
   Optional<Integer> getAppointmentCountBySession(String session) throws Exception;
}
