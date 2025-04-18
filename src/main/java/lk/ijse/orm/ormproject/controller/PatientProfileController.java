package lk.ijse.orm.ormproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lk.ijse.orm.ormproject.dto.PatientDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class PatientProfileController {

    @Setter
    @Getter
    private PatientTableFormController patientTableFormController;

    @FXML
    private Label lblAddress;

    @FXML
    private Label lblAge;

    @FXML
    private Label lblDOB;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblID;

    @FXML
    private Label lblName;

    @FXML
    private Label lblProfileTitle;

    @FXML
    private Label lblTelephone;

    @FXML
    private AnchorPane patientProfilePane;


    public void setPatientDetails(PatientDto patient) {
        lblID.setText(patient.getId());
        lblName.setText(patient.getTitle()+". "+patient.getName());
        lblAddress.setText(patient.getHomeAddress());
        lblProfileTitle.setText(patient.getName()+"'s Profile");
        lblTelephone.setText(patient.getTelephone());
        lblDOB.setText(String.valueOf(patient.getDob()));
        lblEmail.setText(patient.getEmailAddress());
        lblAge.setText(String.valueOf(getPatientCurrentAge(patient.getDob())));

    }

    public int getPatientCurrentAge(LocalDate birthDate) {
        return LocalDate.now().getYear() - birthDate.getYear();
    }


    @FXML
    public void btnAppoinmentHistoryOnClick(ActionEvent actionEvent) {
    }
}
