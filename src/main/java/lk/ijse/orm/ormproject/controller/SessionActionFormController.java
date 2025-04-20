package lk.ijse.orm.ormproject.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.orm.ormproject.bo.BoFactory;
import lk.ijse.orm.ormproject.bo.BoTypes;
import lk.ijse.orm.ormproject.bo.custom.TherapySessionBo;
import lk.ijse.orm.ormproject.dto.TherapySessionDto;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SessionActionFormController implements Initializable {

    TherapySessionBo therapySessionBo = BoFactory.getInstance().getBo(BoTypes.THERAPYSESSION);

    @Getter
    @Setter
    private SessionTableFormController sessionTableFormController;

    @FXML
    private Button btnAction;

    @FXML
    private Button btnClear;

    @FXML
    private ComboBox<String> cmbEndAmPm;

    @FXML
    private ComboBox<String> cmbEndHour;

    @FXML
    private ComboBox<String> cmbEndMin;


    @FXML
    private ComboBox<String> cmbStartAmPm;

    @FXML
    private ComboBox<String> cmbStartHour;

    @FXML
    private ComboBox<String> cmbStartMin;


    @FXML
    private ComboBox<String> cmbProgramme;


    @FXML
    private ComboBox<String> cmbTherapist;

    @FXML
    private DatePicker dpDate;

    @FXML
    private Label lblID;

    @FXML
    private Label lblTitle;


    @FXML
    void btnActionOnMouseClicked(MouseEvent event) {
        TherapySessionDto therapySessionDto = new TherapySessionDto();

        therapySessionDto.setId(lblID.getText());
        therapySessionDto.setProgramme(cmbProgramme.getValue());
        therapySessionDto.setTherapist(cmbTherapist.getValue());
        therapySessionDto.setDate(dpDate.getValue());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h.mm a");

        String startTimeStr = cmbStartHour.getValue() +"."+ cmbStartMin.getValue() + " " + cmbStartAmPm.getValue();
        String endTimeStr = cmbEndHour.getValue() +"."+ cmbEndMin.getValue() + " " + cmbEndAmPm.getValue();

        therapySessionDto.setStartTime(LocalTime.parse(startTimeStr.toUpperCase(), formatter));
        therapySessionDto.setEndTime(LocalTime.parse(endTimeStr.toUpperCase(), formatter));
        try {
            boolean isSave = therapySessionBo.saveTherapySession(therapySessionDto);
            if (isSave) {
                AlertUtil.setInformationAlert(getClass(),"","Session scheduled successfully." , true);
            }else {
                AlertUtil.setInformationAlert(getClass(),"","Cannot scheduled session!!!." , false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    void btnClearOnMouseClicked(MouseEvent event) {

    }

    @FXML
    void cmbProgrammeOnSelectAction(ActionEvent event) {

        String selectedProGramme = cmbProgramme.getSelectionModel().getSelectedItem();

        try {
            List<String> allTherapistNamesByTherapyProgram = therapySessionBo.getAllTherapistNamesByTherapyProgram(selectedProGramme);
            cmbTherapist.getItems().clear();
            cmbTherapist.getSelectionModel().clearSelection();

            cmbTherapist.setItems(FXCollections.observableArrayList(allTherapistNamesByTherapyProgram));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void dpDateOnAction(ActionEvent event) {
        LocalDate selectedDate = dpDate.getValue();
        if (selectedDate.isBefore(LocalDate.now())) {
            dpDate.setValue(LocalDate.now());
        }

    }

    @FXML
    void txtEndTimeOnKeyTyped(KeyEvent event) {

    }

    @FXML
    void txtStartTimeOnKeyTyped(KeyEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            lblID.setText(therapySessionBo.generateNewTherapySessionID());
            Optional<List<String>> optionalList = therapySessionBo.getAllProgrammeNamesForScheduleSession();
            cmbEndAmPm.setItems(FXCollections.observableArrayList("am", "pm"));
            cmbStartAmPm.setItems(FXCollections.observableArrayList("am", "pm"));
            cmbEndHour.setItems(FXCollections.observableArrayList(
                    "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
            ));
            cmbStartHour.setItems(FXCollections.observableArrayList(
                    "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
            ));

            cmbStartMin.setItems(FXCollections.observableArrayList("00", "15", "30", "45"));
            cmbEndMin.setItems(FXCollections.observableArrayList("00", "15", "30", "45"));



            if (optionalList.isPresent()) {
                List<String> programmeNames = optionalList.get();
                cmbProgramme.setItems(FXCollections.observableArrayList(programmeNames));
            }

            dpDate.setValue(LocalDate.now());
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.setErrorAlert(getClass(),"","Cannot generate new ID");
        }


    }
}
