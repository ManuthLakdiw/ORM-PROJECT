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

public class DashBoardFormController {

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnUser;

    @FXML
    private Button btnCheck;

    @FXML
    private Button btnProgramme;

    @FXML
    private Button btnTherapist;

    @FXML
    private Button btnPatient;

    @FXML
    private Button btnSession;

    @FXML
    private Button btnAppointment;



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
        NavigationUtil.loadPane(DashBoardFormController.class ,navPane, "user", "/view/userTable.fxml");
    }


    @FXML
    void btnProgrammeOnMouseClicked(MouseEvent event) {
        NavigationUtil.loadPane(DashBoardFormController.class ,navPane, "Programme", "/view/programmeTable.fxml");
    }

    @FXML
    void btnTherapistOnMouseClicked(MouseEvent event) {
        NavigationUtil.loadPane(DashBoardFormController.class ,navPane, "Therapist", "/view/therapistTable.fxml");

    }


    @FXML
    void btnPatientOnMouseClicked(MouseEvent event) {
        NavigationUtil.loadPane(DashBoardFormController.class ,navPane, "Patient", "/view/patientTable.fxml");


    }



    public void manageRoleTask(boolean visible) {
        btnCheck.setVisible(visible);
        btnCheck.setManaged(visible);
        btnProgramme.setVisible(visible);
        btnProgramme.setManaged(visible);


    }

    @FXML
    void btnSessionOnMouseClicked(MouseEvent mouseEvent) {
        NavigationUtil.loadPane(DashBoardFormController.class ,navPane, "Session", "/view/sessionTable.fxml");

    }

    @FXML
    public void btnAppointmentOnMouseClicked(MouseEvent mouseEvent) {
        NavigationUtil.loadPane(DashBoardFormController.class ,navPane, "Session", "/view/appointmentTable.fxml");

    }
}
