package lk.ijse.orm.ormproject.bo.custom.impl;

import lk.ijse.orm.ormproject.bo.custom.UserBo;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class UserBoImpl implements UserBo {

    @Override
    public boolean verifyUser(String username, String password) {
        return false;
    }
}
