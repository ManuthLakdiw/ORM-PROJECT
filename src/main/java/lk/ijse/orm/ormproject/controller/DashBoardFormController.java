package lk.ijse.orm.ormproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lk.ijse.orm.ormproject.util.NavigationUtil;

public class DashBoardFormController {

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnUser;

    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private Pane navPane;

    @FXML
    void btnLogoutOnMouseClicked(MouseEvent event) {
        try {
            NavigationUtil.getNewStage(
                    (Stage)dashboardPane.getScene().getWindow(),
                    DashBoardFormController.class,
                    "Login",
                    "/view/login.fxml"
                    );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnUserOnMouseClicked(MouseEvent event) {
        NavigationUtil.loadPane(DashBoardFormController.class ,navPane, "user", "/view/user.fxml");
    }

}
