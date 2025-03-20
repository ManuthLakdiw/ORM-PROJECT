package lk.ijse.orm.ormproject.bo.custom.impl;

import lk.ijse.orm.ormproject.bo.custom.UserBo;
import lk.ijse.orm.ormproject.bo.exception.UserNameDuplicateException;
import lk.ijse.orm.ormproject.dao.DaoFactory;
import lk.ijse.orm.ormproject.dao.DaoTypes;
import lk.ijse.orm.ormproject.dao.custom.UserDao;
import lk.ijse.orm.ormproject.dto.UserDto;
import lk.ijse.orm.ormproject.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class UserBoImpl implements UserBo {

    UserDao userDao = DaoFactory.getInstance().getDao(DaoTypes.USER);

    @Override
    public boolean saveUser(UserDto userDto) throws Exception {
        String encodedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getName());
        user.setPassword(encodedPassword);
        user.setRole(userDto.getRole());
        return userDao.save(user);

    }

    @Override
    public List<UserDto> getAllUsers() throws Exception {
        List<UserDto> userDtos = new ArrayList<>();
        List<User> userEntities = userDao.getAll();
            for (User user : userEntities) {
                UserDto userDto = new UserDto();
                userDto.setId(user.getId());
                userDto.setName(user.getUsername());
                userDto.setRole(user.getRole());
                userDtos.add(userDto);
            }
            return userDtos;
    }

    @Override
    public boolean verifyUser(String username, String password) throws Exception {
           Optional<User> user = userDao.getUserByUserName(username);
           if (user.isPresent() && username.equals(user.get().getUsername())) {
               return BCrypt.checkpw(password, user.get().getPassword());

           }
           return false;
    }

    @Override
    public String generateNewID() throws Exception {
        Optional<String> lastID = userDao.lastPK();
        if (lastID.isPresent()) {
            String substring = lastID.get().substring(1);
            int number = Integer.parseInt(substring);
            int newId = number + 1;
            return String.format("U%03d", newId);
        } else {
            return "U001";
        }
    }

    public boolean validUserName(String username) throws Exception {
        boolean notUser = true;
        List<User> userLists = userDao.getAll();

        for (User user : userLists) {
            if (user.getUsername().equals(username)) {
                notUser = false;
                throw new UserNameDuplicateException("Duplicate Entry!!!\nThis username is already taken. Please try a different one.");
            }
        }
        return notUser;
    }

    @Override
    public boolean deleteUser(String id) throws Exception {
        return userDao.delete(id);
    }

    @Override
    public boolean updateUser(UserDto userDto) throws Exception {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getName());
        user.setRole(userDto.getRole());
        Optional<String> password = Optional.ofNullable(userDto.getPassword());
        if (password.isPresent()) {
            String encodePw = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());
            user.setPassword(encodePw);
        }
        return userDao.update(user);
    }

    @Override
    public String getUserNameById(String id) throws Exception {
        List<User> userList = userDao.getAll();

        for (User user : userList) {
            if (id.equals(user.getId())) {
                return user.getUsername();
            }
        }
        return null;
    }

    @Override
    public String getUserRoleByName(String userName) throws Exception {
        List<User> allUsers = userDao.getAll();
        for (User user : allUsers) {
            if (user.getUsername().equals(userName)) {
                return user.getRole();
            }
        }
        return null;
    }
}
