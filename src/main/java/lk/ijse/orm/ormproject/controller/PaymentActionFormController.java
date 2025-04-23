package lk.ijse.orm.ormproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import lk.ijse.orm.ormproject.bo.custom.PaymentBo;
import lk.ijse.orm.ormproject.bo.custom.ProgrammeBo;
import lk.ijse.orm.ormproject.bo.custom.TherapySessionBo;
import lk.ijse.orm.ormproject.dto.AppointmentDto;
import lk.ijse.orm.ormproject.dto.PaymentDto;
import lk.ijse.orm.ormproject.dto.ProgrammeDto;
import lk.ijse.orm.ormproject.dto.TherapySessionDto;
import lk.ijse.orm.ormproject.exception.MissingFieldException;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lk.ijse.orm.ormproject.util.RegexUtil;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentActionFormController implements Initializable {

    PaymentBo paymentBo = BoFactory.getInstance().getBo(BoTypes.PAYMENT);
    TherapySessionBo therapySessionBo = BoFactory.getInstance().getBo(BoTypes.THERAPYSESSION);
    ProgrammeBo programmeBo = BoFactory.getInstance().getBo(BoTypes.PROGRAMME);

    @Getter
    @Setter
    private PaymentTableFormController paymentTableFormController;

    @FXML
    private Button btnAction;

    @FXML
    private Button btnClear;

    @FXML
    private ComboBox<String> cmbAppointment;

    @FXML
    private Label lblBalance;

    @FXML
    private Label lblID;


    @FXML
    private Label lblFee;


    @FXML
    private Label lblPatient;

    @FXML
    private Label lblSession;

    @FXML
    private Label lblSessionFee;

    @FXML
    private Label lblTitle;

    @FXML
    private TextField txtPaidAmount;

    @FXML
    void btnActionOnMouseClicked(MouseEvent event) {
        if (btnAction.getText().equalsIgnoreCase("Save")) {

            if (isSaveOrUpdateEnable()) {

                if (!txtPaidAmount.getText().matches(RegexUtil.feeRegex)){
                    AlertUtil.setInformationAlert(getClass(),"","Can't save!\nFee is not valid!" , false);
                    return;
                }

                PaymentDto paymentDto = new PaymentDto();

                paymentDto.setId(lblID.getText());
                paymentDto.setAppointment(cmbAppointment.getValue());
                paymentDto.setPatient(lblPatient.getText());
                BigDecimal sessionFee = new BigDecimal(lblSessionFee.getText());
                paymentDto.setSessionFee(sessionFee);
                BigDecimal paidAmount = new BigDecimal(txtPaidAmount.getText());
                paymentDto.setPaidAmount(paidAmount);
                BigDecimal balance = new BigDecimal(lblBalance.getText());
                paymentDto.setDueAmount(balance);
                paymentDto.setDate(LocalDate.now());

                try {
                    boolean isSaved = paymentBo.savePayment(paymentDto);
                    if (isSaved) {
                        paymentTableFormController.loadPaymentTable();
                        resetFields();
                        AlertUtil.setInformationAlert(getClass(),"","Payment added successfully!" , false);
                    }else {
                        AlertUtil.setInformationAlert(getClass(),"","Cannot add payment!!!." , false);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else {
                AlertUtil.setInformationAlert(
                        getClass(),
                        "",
                        new MissingFieldException("Required field is missing.\nPlease provide all necessary information.").getMessage()
                        ,true
                );
            }
        } else {

            if (isSaveOrUpdateEnable()) {

                if (!txtPaidAmount.getText().matches(RegexUtil.feeRegex)){
                    AlertUtil.setInformationAlert(getClass(),"","Can't update!\nFee is not valid!" , false);
                    return;
                }

                PaymentDto paymentDto = new PaymentDto();

                paymentDto.setId(lblID.getText());
                paymentDto.setAppointment(cmbAppointment.getValue());
                paymentDto.setPatient(lblPatient.getText());
                BigDecimal sessionFee = new BigDecimal(lblSessionFee.getText());
                paymentDto.setSessionFee(sessionFee);
                BigDecimal paidAmount = new BigDecimal(txtPaidAmount.getText());
                paymentDto.setPaidAmount(paidAmount);
                BigDecimal balance = new BigDecimal(lblBalance.getText());
                paymentDto.setDueAmount(balance);
                paymentDto.setDate(LocalDate.now());


                try {
                    boolean isUpdate = paymentBo.updatePayment(paymentDto);
                    if (isUpdate) {
                        paymentTableFormController.loadPaymentTable();
                        resetFields();
                        AlertUtil.setInformationAlert(getClass(),"","Payment updated successfully." , true);
                        Stage stage = (Stage) btnAction.getScene().getWindow();
                        stage.close();
                    }else {
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Can't update Payment Schedule", false);
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
    void cmbAppointmentOnAction(ActionEvent event) {
        String selectedValue = cmbAppointment.getValue();


        String sessionProgramme = "";

        try {
            List<AppointmentDto> allAppointmentsForPayments = paymentBo.getAllAppointmentsForPayments();
            for (AppointmentDto appointmentDto : allAppointmentsForPayments) {
                if (appointmentDto.getId().equals(selectedValue)) {
                    lblPatient.setText(appointmentDto.getPatient());
                    lblSession.setText(appointmentDto.getSession());
                    TherapySessionDto exitingSession = therapySessionBo.getSession(appointmentDto.getSession());
                    System.out.println("Exiting session . get program : " + exitingSession.getProgramme());
                    ProgrammeDto programme = programmeBo.getProgramme(exitingSession.getProgramme());
                    lblSessionFee.setText(programme.getFee().toString());


                    break;
                }
            }

            List<ProgrammeDto> allProgrammesForPayments = paymentBo.getAllProgrammesForPayments();
            for (ProgrammeDto programmeDto : allProgrammesForPayments) {
                if (programmeDto.getId().equals(sessionProgramme)) {
                    lblSessionFee.setText(programmeDto.getFee().toString());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void txtPaidAmountOnKeyTyped(KeyEvent event) {
        if (txtPaidAmount.getText().isEmpty()) {
            RegexUtil.resetErrorStyle(txtPaidAmount);
            lblFee.setText("");
            lblBalance.setText("");
            return;
        }


        if (!txtPaidAmount.getText().matches(RegexUtil.feeRegex)){
            RegexUtil.setErrorStyle(false, txtPaidAmount);
            lblFee.setText("Invalid fee ‼️");
            lblBalance.setText("");

        }else {
            RegexUtil.resetErrorStyle(txtPaidAmount);
            lblFee.setText("");
            BigDecimal paidAmount = new BigDecimal(txtPaidAmount.getText());
            BigDecimal sessionFee = new BigDecimal(lblSessionFee.getText());

            if (!(paidAmount.compareTo(sessionFee) > 0)) {
                BigDecimal balance = sessionFee.subtract(paidAmount);
                lblBalance.setText(balance.toString());

            }else {
                lblBalance.setText("");
            }

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            lblID.setText(paymentBo.generateNewPaymentID());

            List<AppointmentDto> allAppointmentsForPayments = paymentBo.getAllAppointmentsForPayments();
            List<String> appointmentIds = new ArrayList<>();
            for (AppointmentDto appointmentDto : allAppointmentsForPayments) {
                appointmentIds.add(appointmentDto.getId());
            }
            ObservableList<String> appointmentObservableList = FXCollections.observableArrayList(appointmentIds);
            cmbAppointment.setItems(appointmentObservableList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isSaveOrUpdateEnable() {
        return cmbAppointment.getSelectionModel().getSelectedItem() != null &&
                !txtPaidAmount.getText().isEmpty();
    }

    public void resetFields() {
        try {
            lblID.setText(paymentBo.generateNewPaymentID());
            cmbAppointment.getSelectionModel().clearSelection();
            lblFee.setText("");
            lblBalance.setText("");
            lblSession.setText("");
            lblSessionFee.setText("");
            lblPatient.setText("");
            txtPaidAmount.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPaymentData(PaymentDto paymentDto) {
        lblID.setText(paymentDto.getId());
        cmbAppointment.getSelectionModel().select(paymentDto.getAppointment());
        txtPaidAmount.setText(paymentDto.getPaidAmount().toString());
        lblBalance.setText(paymentDto.getDueAmount().toString());

    }

    public void setActionButtonText(String update) {
        btnAction.setText(update);
    }

    public void setLblTitle(String scheduleUpdate) {
        lblTitle.setText(scheduleUpdate);
    }
}
