module lk.ijse.orm.ormproject {

    requires javafx.fxml;
    requires lombok;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires jbcrypt;
    requires java.desktop;
    requires com.jfoenix;
    requires javafx.controls;
    requires java.mail;
    requires java.management;
    requires org.controlsfx.controls;
    requires fontawesomefx;

    opens lk.ijse.orm.ormproject.controller to javafx.fxml;
    exports lk.ijse.orm.ormproject;


    opens lk.ijse.orm.ormproject.config to jakarta.persistence;
    opens lk.ijse.orm.ormproject.dto.tm to javafx.base;
    opens lk.ijse.orm.ormproject.dto to javafx.fxml;

    opens lk.ijse.orm.ormproject.entity to org.hibernate.orm.core;
}