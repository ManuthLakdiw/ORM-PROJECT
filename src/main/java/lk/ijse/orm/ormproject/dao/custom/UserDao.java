package lk.ijse.orm.ormproject.dao.custom;

import lk.ijse.orm.ormproject.dao.CrudDao;
import lk.ijse.orm.ormproject.entity.User;

import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public interface UserDao extends CrudDao<User, String> {

    Optional<User> getUserByUserName(String username) throws Exception;


}
