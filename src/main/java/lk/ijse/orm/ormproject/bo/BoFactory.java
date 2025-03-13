package lk.ijse.orm.ormproject.bo;

import lk.ijse.orm.ormproject.bo.custom.impl.UserBoImpl;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class BoFactory {

    private static BoFactory boFactory;

    private BoFactory() {}

    public static BoFactory getInstance() {
        return boFactory == null ? boFactory = new BoFactory() : boFactory;
    }

    @SuppressWarnings("unchecked")
    public <T extends SuperBo>T getBo(BoTypes boType) {

        return switch (boType){
            case USER -> (T) new UserBoImpl();
        };
    }

}
