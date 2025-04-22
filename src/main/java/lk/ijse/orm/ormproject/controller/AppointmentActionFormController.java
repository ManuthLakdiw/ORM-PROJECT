package lk.ijse.orm.ormproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.orm.ormproject.bo.BoFactory;
import lk.ijse.orm.ormproject.bo.BoTypes;
import lk.ijse.orm.ormproject.bo.custom.AppointmentBo;
import lk.ijse.orm.ormproject.dto.AppointmentDto;
import lk.ijse.orm.ormproject.dto.PatientDto;
import lk.ijse.orm.ormproject.dto.TherapySessionDto;
import lk.ijse.orm.ormproject.exception.MissingFieldException;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AppointmentActionFormController implements Initializable {

    AppointmentBo appointmentBo = BoFactory.getInstance().getBo(BoTypes.APPOINTMENT);

    @Getter
    @Setter
    private AppointmentTableFormController appointmentTableFormController;

    @FXML
    private Button btnAction;

    @FXML
    private Button btnClear;


    @FXML
    private ComboBox<String> cmbPatient;

    @FXML
    private ComboBox<String> cmbSession;

    @FXML
    private Label lblID;

    @FXML
    private Label lblEndTime;

    @FXML
    private Label lblStartTime;


    @FXML
    private Label lblScheduledDate;

    @FXML
    private Label lblPatientName;

    @FXML
    private Label lblSessionName;

    @FXML
    private Label lblTherapist;

    @FXML
    private Label lblTitle;

    @FXML
    void btnActionOnMouseClicked(MouseEvent event) {

        if (btnAction.getText().equalsIgnoreCase("Save")) {
            if (isSaveOrUpdate()) {
                AppointmentDto appointmentDto = new AppointmentDto();


                appointmentDto.setId(lblID.getText());
                appointmentDto.setPatient(cmbPatient.getValue());
                appointmentDto.setSession(cmbSession.getValue());
                appointmentDto.setDate(LocalDate.parse(lblScheduledDate.getText()));
                appointmentDto.setTime(LocalTime.now());

                try {
                    boolean isSaved = appointmentBo.saveAppointment(appointmentDto);
                    if (isSaved) {
                        resetFields();
                        AlertUtil.setInformationAlert(getClass(),"","Appointment booking successfully." , true);
                    }else {
                        AlertUtil.setInformationAlert(getClass(),"","Cannot booking appointment!!!." , false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else {
                AlertUtil.setInformationAlert(
                        getClass(),
                        "",
                        new MissingFieldException("Required field is missing.\nPlease provide all necessary information.").getMessage()
                        ,true
                );
            }
        }else {
            if (isSaveOrUpdate()) {
                AppointmentDto appointmentDto = new AppointmentDto();
                appointmentDto.setId(lblID.getText());
                appointmentDto.setPatient(cmbPatient.getValue());
                appointmentDto.setSession(cmbSession.getValue());
                appointmentDto.setDate(LocalDate.parse(lblScheduledDate.getText()));
                appointmentDto.setTime(LocalTime.now());

                try {
                    boolean isUpdate = appointmentBo.updateAppointment(appointmentDto);
                    if (isUpdate) {
                        resetFields();
                        appointmentTableFormController.loadTable();
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Appointment updated successfully!", true);
                        Stage stage = (Stage) btnAction.getScene().getWindow();
                        stage.close();
                    } else {
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Can't update Appointment", false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


    }

    @FXML
    void btnClearOnMouseClicked(MouseEvent event) {
        resetFields();

    }

    @FXML
    void cmbPatientOnSelectAction(ActionEvent event) {
        String selectedValue = cmbPatient.getSelectionModel().getSelectedItem();
        try {
            List<PatientDto> allPatientsForAppointment = appointmentBo.getAllPatientsForAppointment();
            for (PatientDto patientDto : allPatientsForAppointment) {
                if (patientDto.getId().equals(selectedValue)) {
                    if (selectedValue != null) {
                        lblPatientName.setText(patientDto.getName());
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    void cmbSessionOnSelectAction(ActionEvent event) {
        String selectedValue = cmbSession.getSelectionModel().getSelectedItem();
        try {
            List<TherapySessionDto> allSessionsForAppointment = appointmentBo.getAllSessionsForAppointment();
            for (TherapySessionDto therapySessionDto : allSessionsForAppointment) {
                if (therapySessionDto.getId().equals(selectedValue)) {
                    if (selectedValue != null) {
                        lblSessionName.setText(therapySessionDto.getProgramme());
                        lblTherapist.setText(therapySessionDto.getTherapist());
                        lblScheduledDate.setText(String.valueOf(therapySessionDto.getDate()));
                        lblEndTime.setText(String.valueOf(therapySessionDto.getEndTime()));
                        lblStartTime.setText(String.valueOf(therapySessionDto.getStartTime()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            lblID.setText(appointmentBo.generateNewAppointmentID());
            List<PatientDto> allPatientsForAppointment = appointmentBo.getAllPatientsForAppointment();

            List<String> patientIds = new ArrayList<>();
            for (PatientDto patientDto : allPatientsForAppointment) {
                patientIds.add(patientDto.getId());
            }

            ObservableList<String> patientObservableList = FXCollections.observableArrayList(patientIds);
            cmbPatient.setItems(patientObservableList);


            List<TherapySessionDto> allSessionsForAppointment = appointmentBo.getAllSessionsForAppointment();

            List<String> sessionIds = new ArrayList<>();
            for (TherapySessionDto therapySessionDto : allSessionsForAppointment) {
                sessionIds.add(therapySessionDto.getId());
            }

            ObservableList<String> sessionObservableList = FXCollections.observableArrayList(sessionIds);
            cmbSession.setItems(sessionObservableList);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void resetFields() {
        try {
            lblID.setText(appointmentBo.generateNewAppointmentID());
            cmbPatient.getSelectionModel().clearSelection();
            cmbSession.getSelectionModel().clearSelection();
            lblPatientName.setText("");
            lblSessionName.setText("");
            lblTherapist.setText("");
            lblScheduledDate.setText("");

            lblEndTime.setText("");
            lblStartTime.setText("");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isSaveOrUpdate() {
        return cmbPatient.getSelectionModel().getSelectedItem() != null &&
                cmbSession.getSelectionModel().getSelectedItem() != null;
    }

    public void setPatientActionBtnText(String update) {
        btnAction.setText(update);
    }

    public void setLblTitle(String patientUpdate) {
        lblTitle.setText(patientUpdate);
    }

    public void setAppoinmentData(AppointmentDto appointmentDto) {
        lblID.setText(appointmentDto.getId());
        cmbSession.getSelectionModel().select(appointmentDto.getSession());
        cmbPatient.getSelectionModel().select(appointmentDto.getPatient());
    }
}
