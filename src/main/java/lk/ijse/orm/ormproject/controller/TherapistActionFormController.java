package lk.ijse.orm.ormproject.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.orm.ormproject.bo.BoFactory;
import lk.ijse.orm.ormproject.bo.BoTypes;
import lk.ijse.orm.ormproject.bo.custom.TherapistBo;
import lk.ijse.orm.ormproject.dto.TherapistDto;
import lk.ijse.orm.ormproject.exception.MissingFieldException;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lk.ijse.orm.ormproject.util.RegexUtil;
import lombok.Getter;
import lombok.Setter;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TherapistActionFormController implements Initializable {

    TherapistBo therapistBo = BoFactory.getInstance().getBo(BoTypes.THERAPIST);


    @Getter
    @Setter
    private TherapistTableFromController therapistTableFromController;


    @FXML
    private Button btnAction;

    @FXML
    private Button btnClear;

    @FXML
    private ComboBox<String> cmbProgramme;

    @FXML
    private Label lblID;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblValidateEmail;

    @FXML
    private Label lblValidateName;

    @FXML
    private Label lblValidateTelephone;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtTelephone;

    @FXML
    void btnActionOnMouseClicked(MouseEvent event) {

        if (btnAction.getText().equalsIgnoreCase("Save")) {

            if (isFillAllFields()) {

                if (!txtName.getText().matches(RegexUtil.nameRegex)) {
                    AlertUtil.setInformationAlert(getClass(),"","Can't save!\nName is not valid!" , false);
                    return;
                }

                if (!txtTelephone.getText().matches(RegexUtil.phoneNumberRegex)) {
                    AlertUtil.setInformationAlert(getClass(),"","Can't save!\nPhoneNumber is not valid!" , false);
                    return;
                }

                if (!txtEmail.getText().matches(RegexUtil.emailRegex)) {
                    AlertUtil.setInformationAlert(getClass(),"","Can't save!\nEmail Address is not valid!" , false);
                    return;
                }

                TherapistDto therapistDto = new TherapistDto();
                therapistDto.setId(lblID.getText());
                therapistDto.setName("Dr. "+txtName.getText());
                therapistDto.setTelephone(txtTelephone.getText());
                therapistDto.setEmail(txtEmail.getText());
                therapistDto.setProgramme(cmbProgramme.getValue());



                try {
                    boolean isSave = therapistBo.saveTherapist(therapistDto);
                    if(isSave){
                        resetFields();
                        lblID.setText(therapistBo.generateNewTherapistID());
                        therapistTableFromController.loadTherapistTable();
                        AlertUtil.setInformationAlert(getClass(),"","Therapist saved successfully." , true);
                    }else {
                        AlertUtil.setInformationAlert(getClass(),"","Cannot save Therapist!!!." , false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                AlertUtil.setInformationAlert(
                        getClass(),
                        "",
                        new MissingFieldException("Required field is missing.\nPlease provide all necessary information.").getMessage()
                        ,true
                );
            }
        } else {

            if (isFillAllFields()) {

                if (!txtName.getText().matches(RegexUtil.nameRegex)) {
                    AlertUtil.setInformationAlert(getClass(),"","Can't update!\nName is not valid!" , false);
                    return;
                }

                if (!txtTelephone.getText().matches(RegexUtil.phoneNumberRegex)) {
                    AlertUtil.setInformationAlert(getClass(),"","Can't update!\nPhoneNumber is not valid!" , false);
                    return;
                }

                if (!txtEmail.getText().matches(RegexUtil.emailRegex)) {
                    AlertUtil.setInformationAlert(getClass(),"","Can't update!\nEmail Address is not valid!" , false);
                    return;
                }

                TherapistDto therapistDto = new TherapistDto();
                therapistDto.setId(lblID.getText());
                therapistDto.setName("Dr. "+txtName.getText());
                therapistDto.setTelephone(txtTelephone.getText());
                therapistDto.setEmail(txtEmail.getText());
                therapistDto.setProgramme(cmbProgramme.getValue());

                try {
                    boolean isUpdated = therapistBo.updateTherapist(therapistDto);
                    if(isUpdated){
                        resetFields();
                        therapistTableFromController.loadTherapistTable();
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Therapist updated successfully!", true);
                        Stage stage = (Stage) btnAction.getScene().getWindow();
                        stage.close();
                    }else {
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Can't update Therapist", false);
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
        }
    }

    @FXML
    void btnClearOnMouseClicked(MouseEvent event) {
        resetFields();
    }

    @FXML
    void txtEmailOnKeyTyped(KeyEvent event) {


        if (txtEmail.getText().isEmpty()) {
            lblValidateEmail.setText("");
            RegexUtil.resetErrorStyle(txtEmail);
            return;
        }

        if (!txtEmail.getText().matches(RegexUtil.emailRegex)) {
            lblValidateEmail.setText("Invalid email format ‼️");
            RegexUtil.setErrorStyle(false,txtEmail);
        } else {
            lblValidateEmail.setText("");
            RegexUtil.resetErrorStyle(txtEmail);
        }

    }

    @FXML
    void txtNameOnKeyTyped(KeyEvent keyEvent) {
        if (txtName.getText().isEmpty()) {
            lblValidateName.setText("");
            return;
        }
        if (!txtName.getText().matches(RegexUtil.nameRegex)) {
            lblValidateName.setText("Invalid name format");
            RegexUtil.setErrorStyle(false,txtName);

        }else {
            lblValidateName.setText("");
            RegexUtil.resetErrorStyle(txtName);
        }
    }

    @FXML
    void txtTelephoneOnKeyTyped(KeyEvent keyEvent) {
        if (txtTelephone.getText().isEmpty()) {
            lblValidateTelephone.setText("");
            RegexUtil.resetErrorStyle(txtTelephone);
            return;
        }
        if (!txtTelephone.getText().matches(RegexUtil.phoneNumberRegex)) {
            lblValidateTelephone.setText("Invalid phone number format");
            RegexUtil.setErrorStyle(false,txtTelephone);

        }else {
            lblValidateTelephone.setText("");
            RegexUtil.resetErrorStyle(txtTelephone);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(()->{
            txtName.requestFocus();
        });
        try {
            lblID.setText(therapistBo.generateNewTherapistID());
            ObservableList<String> programmeList = FXCollections.observableArrayList(therapistBo.getAllProgrammeNamesForAssignTherapists());
            cmbProgramme.setItems(programmeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetFields() {
        txtName.clear();
        txtTelephone.clear();
        txtEmail.clear();
        cmbProgramme.getSelectionModel().clearSelection();

        lblValidateName.setText("");
        lblValidateTelephone.setText("");
        lblValidateEmail.setText("");

        RegexUtil.resetErrorStyle(txtName);
        RegexUtil.resetErrorStyle(txtTelephone);
        RegexUtil.resetErrorStyle(txtEmail);
    }


    private boolean isFillAllFields(){
        return !txtName.getText().isEmpty() &&
                !txtEmail.getText().isEmpty() &&
                !txtTelephone.getText().isEmpty() &&
                cmbProgramme.getSelectionModel().getSelectedItem() != null;
    }


    public void setActionButtonText(String text) {
        btnAction.setText(text);
    }

    public void setTherapistData(TherapistDto therapistDto) {
        lblID.setText(therapistDto.getId());
        String removeDrFromName = therapistDto.getName().substring(4);
        txtName.setText(removeDrFromName);
        txtTelephone.setText(therapistDto.getTelephone());
        Platform.runLater(() -> {
            txtName.positionCaret(txtName.getText().length());
        });
        txtEmail.setText(therapistDto.getEmail());
        cmbProgramme.getSelectionModel().select(therapistDto.getProgramme());
    }

    public void setLblTitle(String title) {
        lblTitle.setText(title);
    }
}
