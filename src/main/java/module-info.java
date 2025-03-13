module lk.ijse.orm.ormproject {

    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;





    opens lk.ijse.orm.ormproject.controller to javafx.fxml;
    exports lk.ijse.orm.ormproject;
}