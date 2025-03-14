module lk.ijse.orm.ormproject {

    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires jbcrypt;

    opens lk.ijse.orm.ormproject.controller to javafx.fxml;
    exports lk.ijse.orm.ormproject;


    opens lk.ijse.orm.ormproject.config to jakarta.persistence;
    opens lk.ijse.orm.ormproject.entity to org.hibernate.orm.core;
}