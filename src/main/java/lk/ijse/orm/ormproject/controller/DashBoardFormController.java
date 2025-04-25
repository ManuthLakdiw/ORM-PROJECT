package lk.ijse.orm.ormproject.controller;

import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lk.ijse.orm.ormproject.util.NavigationUtil;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardFormController implements Initializable {

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
    private Button btnPayment;

    @FXML
    private Label lblHome;



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

    @FXML
    void btnPaymentOnMouseClicked(MouseEvent event) {
        NavigationUtil.loadPane(DashBoardFormController.class ,navPane, "Payment", "/view/paymentTable.fxml");


    }

    @FXML
    void btnSessionOnMouseClicked(MouseEvent mouseEvent) {
        NavigationUtil.loadPane(DashBoardFormController.class ,navPane, "Session", "/view/sessionTable.fxml");

    }

    @FXML
    public void btnAppointmentOnMouseClicked(MouseEvent mouseEvent) {
        NavigationUtil.loadPane(DashBoardFormController.class ,navPane, "Appointment", "/view/appointmentTable.fxml");

    }



    public void manageRoleTask(boolean visible) {
        btnProgramme.setVisible(visible);
        btnProgramme.setManaged(visible);
        btnTherapist.setVisible(visible);
        btnTherapist.setManaged(visible);
        btnUser.setVisible(visible);
        btnUser.setManaged(visible);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogout.setGraphic(MaterialIconFactory.get().createIcon(MaterialIcon.ARROW_BACK, "25px"));
        lblHome.setGraphic(MaterialIconFactory.get().createIcon(MaterialIcon.HOME, "45px"));
        lblHome.setTooltip(new Tooltip("Home"));
        lblHome.setStyle("-fx-cursor: hand");


        btnUser.setTooltip(new Tooltip("User"));
        btnUser.setStyle("-fx-cursor: hand");
        btnTherapist.setTooltip(new Tooltip("Therapist"));
        btnTherapist.setStyle("-fx-cursor: hand");
        btnPatient.setTooltip(new Tooltip("Patient"));
        btnPatient.setStyle("-fx-cursor: hand");
        btnProgramme.setTooltip(new Tooltip("Programme"));
        btnProgramme.setStyle("-fx-cursor: hand");
        btnSession.setTooltip(new Tooltip("Session"));
        btnSession.setStyle("-fx-cursor: hand");
        btnAppointment.setTooltip(new Tooltip("Appointment"));
        btnAppointment.setStyle("-fx-cursor: hand");
        btnPayment.setTooltip(new Tooltip("Payment"));
        btnPayment.setStyle("-fx-cursor: hand");
        btnLogout.setTooltip(new Tooltip("Logout"));
        btnLogout.setStyle("-fx-cursor: hand");



    }
}
