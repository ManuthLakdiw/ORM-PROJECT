package lk.ijse.orm.ormproject.bo.custom;

import lk.ijse.orm.ormproject.bo.SuperBo;
import lk.ijse.orm.ormproject.bo.exception.UserNameDuplicateException;
import lk.ijse.orm.ormproject.dto.UserDto;
import lk.ijse.orm.ormproject.entity.User;

import java.util.List;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public interface UserBo extends SuperBo {

    boolean saveUser(UserDto userDto) throws Exception;
    List<UserDto> getAllUsers() throws Exception;
    boolean verifyUser(String username, String password) throws Exception;
    String generateNewID() throws Exception;
    boolean validUserName(String username) throws Exception;
    boolean deleteUser(String id) throws Exception;
    boolean updateUser(UserDto userDto) throws Exception;
    String getUserNameById(String id) throws Exception;
    String getUserRoleByName(String userName) throws Exception;


}
