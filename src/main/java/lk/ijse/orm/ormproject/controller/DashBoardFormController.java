package lk.ijse.orm.ormproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lk.ijse.orm.ormproject.util.NavigationUtil;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardFormController {

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnUser;

    @FXML
    private Button btnCheck;

    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private Pane navPane;


    @Setter
    private LoginFormController loginFormController;

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


    public void manageRoleTask(boolean visible) {
        btnCheck.setVisible(visible);
    }
}
