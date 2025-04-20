package lk.ijse.orm.ormproject.controller;

import javafx.application.Platform;
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
import lk.ijse.orm.ormproject.bo.custom.ProgrammeBo;
import lk.ijse.orm.ormproject.dto.ProgrammeDto;
import lk.ijse.orm.ormproject.exception.MissingFieldException;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lombok.Setter;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class ProgrammeActionFormController implements Initializable {

    @FXML
    private Button btnAction;

    @FXML
    private Button btnClear;

    @FXML
    private ComboBox<String> cmbDuration;

    @FXML
    private Label lblID;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblFee;

    @FXML
    private TextField  txtFee;

    @FXML
    private TextField txtProgrammeName;

    ProgrammeBo programmeBo = BoFactory.getInstance().getBo(BoTypes.PROGRAMME);

    @Setter
    private ProgrammeTableFormController programmeTableFormController;

    String feeRegex = "^\\d{1,10}(\\.\\d{1,2})?$";

    @FXML
    void btnActionOnMouseClicked(MouseEvent event) {
        if (btnAction.getText().equalsIgnoreCase("Save")) {

            if (isAllFieldHasValueForSaveOrUpdate()){

                if (!txtFee.getText().matches(feeRegex)){
                    AlertUtil.setInformationAlert(getClass(),"","Can't save!\nFee is not valid!" , false);
                    return;
                }

                ProgrammeDto programmeDto = new ProgrammeDto();
                programmeDto.setId(lblID.getText());
                programmeDto.setProgrammeName(txtProgrammeName.getText());
                programmeDto.setDuration(cmbDuration.getSelectionModel().getSelectedItem());
                programmeDto.setFee(new BigDecimal(txtFee.getText()));


                try {
                    boolean isSave = programmeBo.saveProgramme(programmeDto);
                    if (isSave){
                        programmeTableFormController.loadProgrammeTable();
                        clearFields();
                        AlertUtil.setInformationAlert(getClass(),"","Programme saved successfully." , true);


                    }else {
                        AlertUtil.setInformationAlert(getClass(),"","Cannot save programme!!!." , false);
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

            if (isAllFieldHasValueForSaveOrUpdate()) {

                if (!txtFee.getText().matches(feeRegex)){
                    AlertUtil.setInformationAlert(getClass(),"","Can't update!\nFee is not valid!" , false);
                    return;
                }

                ProgrammeDto programmeDto = new ProgrammeDto();
                programmeDto.setId(lblID.getText());
                programmeDto.setProgrammeName(txtProgrammeName.getText());
                programmeDto.setDuration(cmbDuration.getSelectionModel().getSelectedItem());
                programmeDto.setFee(new BigDecimal(txtFee.getText()));


                try {
                    boolean isUpdate = programmeBo.updateProgramme(programmeDto);

                    if (isUpdate){
                        clearFields();
                        programmeTableFormController.loadProgrammeTable();
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Programme updated successfully!", true);
                        Stage stage = (Stage) btnAction.getScene().getWindow();
                        stage.close();

                    }else {
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Can't update programme", false);
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

    }

    @FXML
    void txtFeeOnKeyTyped(KeyEvent event) {

        if (txtFee.getText().isEmpty()) {
            lblFee.setText("");
            return;
        }


        if (!txtFee.getText().matches(feeRegex)){
            lblFee.setText("Invalid fee ‼️");
        }else {
            lblFee.setText("");
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            lblID.setText(programmeBo.generateNewProgrammeID());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        Platform.runLater(() -> {
            txtProgrammeName.requestFocus();
        });
        String[] duration = {"15min","30min","45min","1hour","75min","90min","105min","2hour"};

        cmbDuration.getItems().addAll(duration);

    }

    public boolean isAllFieldHasValueForSaveOrUpdate() {
        return !txtProgrammeName.getText().isEmpty() &&
                !txtFee.getText().isEmpty() &&
                cmbDuration.getSelectionModel().getSelectedItem() != null;
    }

    private void clearFields(){
        try {
            lblID.setText(programmeBo.generateNewProgrammeID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtProgrammeName.clear();
        txtFee.clear();
        cmbDuration.getSelectionModel().clearSelection();
    }

    public void setLblTitle(String programmeUpdate) {
        lblTitle.setText(programmeUpdate);
    }

    public void setActionButtonText(String update) {
        btnAction.setText(update);
    }

    public void setProgrammeData(ProgrammeDto programmeDto) {
        lblID.setText(programmeDto.getId());
        txtProgrammeName.setText(programmeDto.getProgrammeName());
        Platform.runLater(() -> {
            txtProgrammeName.positionCaret(txtProgrammeName.getText().length());
        });
        txtFee.setText(programmeDto.getFee().toString());
        cmbDuration.setValue(programmeDto.getDuration());
    }
}
