package lk.ijse.orm.ormproject.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.orm.ormproject.bo.BoFactory;
import lk.ijse.orm.ormproject.bo.BoTypes;
import lk.ijse.orm.ormproject.bo.custom.PatientBo;
import lk.ijse.orm.ormproject.dto.PatientDto;
import lk.ijse.orm.ormproject.exception.MissingFieldException;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lk.ijse.orm.ormproject.util.DateUtil;
import lk.ijse.orm.ormproject.util.RegexUtil;
import lombok.Getter;
import lombok.Setter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class PatientActionFormController implements Initializable {

    @Getter
    @Setter
    private PatientTableFormController patientTableFormController;


    PatientBo patientBo = BoFactory.getInstance().getBo(BoTypes.PATIENT);

    @FXML
    private Button btnAction;

    @FXML
    private Button btnClear;

    @FXML
    private DatePicker dpDOB;

    @FXML
    private Label lblID;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblValidateEmail;

    @FXML
    private Label lblValidateName;

    @FXML
    private Label lblValidatePhoneNumber;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtHomeAddress;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhoneNumber;


    @FXML
    private ComboBox<String> cmbTitle;


    @FXML
    void btnActionOnMouseClicked(MouseEvent event) {
        if (btnAction.getText().equalsIgnoreCase("Save")) {

            String patientEmail;
            String patientName;
            String patientID;

            if (isFillAllFields()) {

                if (!txtName.getText().matches(RegexUtil.nameRegex)) {
                    AlertUtil.setInformationAlert(getClass(),"","Can't save!\nName is not valid!" , false);
                    return;
                }

                if (!txtPhoneNumber.getText().matches(RegexUtil.phoneNumberRegex)) {
                    AlertUtil.setInformationAlert(getClass(),"","Can't save!\nPhoneNumber is not valid!" , false);
                    return;
                }

                if (!txtEmail.getText().matches(RegexUtil.emailRegex)) {
                    AlertUtil.setInformationAlert(getClass(),"","Can't save!\nEmail Address is not valid!" , false);
                    return;
                }

                PatientDto patientDto = new PatientDto();
                patientDto.setId(lblID.getText());
                patientDto.setTitle(cmbTitle.getValue());
                patientDto.setName(txtName.getText());
                patientDto.setDob(dpDOB.getValue());
                patientDto.setHomeAddress(txtHomeAddress.getText());
                patientDto.setTelephone(txtPhoneNumber.getText());
                patientDto.setEmailAddress(txtEmail.getText());

                try {
                    boolean isSaved = patientBo.savePatient(patientDto);
                    if (isSaved) {
                        patientEmail = txtEmail.getText();
                        patientName = cmbTitle.getSelectionModel().getSelectedItem()+". "+txtName.getText();
                        patientID = lblID.getText();
                        Thread newPatientRegister = new Thread(() -> {
                            String from = "serenity.mht.center@gmail.com";
                            String host = "smtp.gmail.com";
                            String username = "serenity.mht.center@gmail.com";
                            String password = "huzc fcve qmzq dohd"; // App password

                            Properties props = new Properties();
                            props.put("mail.smtp.auth", "true");
                            props.put("mail.smtp.starttls.enable", "true");
                            props.put("mail.smtp.host", host);
                            props.put("mail.smtp.port", "587");

                            Session session = Session.getInstance(props, new Authenticator() {
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password);
                                }
                            });

                            try {
                                Message message = new MimeMessage(session);
                                message.setFrom(new InternetAddress(from));
                                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(patientEmail));
                                message.setSubject("Your Registration Notification");

                                String subject = "Welcome to Serenity Mental Health Center";
                                String htmlTemplate = String.format("""
<html>
<head>
    <meta charset="UTF-8">
    <title>%s</title>
    <style>
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(to right, #e0f7fa, #e1bee7);
            margin: 0;
            padding: 30px 0;
            color: #333;
        }

        .container {
            max-width: 650px;
            margin: auto;
            background-color: #fff;
            border-radius: 15px;
            box-shadow: 0 5px 25px rgba(0, 0, 0, 0.1);
            animation: fadeIn 0.8s ease-in-out;
            overflow: hidden;
        }

        .header {
            background: linear-gradient(135deg, #42a5f5, #7e57c2);
            color: #fff;
            padding: 25px 30px;
            text-align: center;
        }

        .header h2 {
            margin: 0;
            font-size: 26px;
        }

        .content {
            padding: 30px 35px;
            line-height: 1.7;
            font-size: 16px;
        }

        .content p {
            margin-bottom: 20px;
        }

        .highlight {
            color: #00897b;
            font-weight: bold;
        }

        .footer {
            background-color: #f8f8f8;
            padding: 20px;
            text-align: center;
            font-size: 13px;
            color: #999;
        }

        .signature {
            margin-top: 25px;
            font-style: italic;
            color: #555;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>%s</h2>
        </div>
        <div class="content">
            <p>Dear <strong>%s</strong>,</p>
            <p>We are pleased to inform you that you have been successfully registered to the <span class="highlight">Serenity Mental Health Center</span>.</p>
            <p><strong>Your Patient ID:</strong> <span class="highlight">%s</span></p>
            <p>If you have any questions or need assistance, feel free to contact us anytime.</p>
            <p class="signature">Warm Regards,<br>Serenity Mental Health Center Team</p>
        </div>
        <div class="footer">
            &copy; 2025 Serenity Center - Auto-generated Email
        </div>
    </div>
</body>
</html>
""", subject, subject, patientName, patientID);



                                message.setContent(htmlTemplate, "text/html");
                                Transport.send(message);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        newPatientRegister.start();
                        resetFields();
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Patient saved successfully!", true);
                        patientTableFormController.loadPatientTable();
                        lblID.setText(patientBo.generateNewPatientID());
                    } else {
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Can't save Patient", false);
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
        }else {
            if (isFillAllFields()) {
                if (!txtName.getText().matches(RegexUtil.nameRegex)) {
                    AlertUtil.setInformationAlert(getClass(),"","Can't Update!\nName is not valid!" , false);
                    return;
                }

                if (!txtPhoneNumber.getText().matches(RegexUtil.phoneNumberRegex)) {
                    AlertUtil.setInformationAlert(getClass(),"","Can't Update!\nPhoneNumber is not valid!" , false);
                    return;
                }

                if (!txtEmail.getText().matches(RegexUtil.emailRegex)) {
                    AlertUtil.setInformationAlert(getClass(),"","Can't Update!\nEmail Address is not valid!" , false);
                    return;
                }

                PatientDto patientDto = new PatientDto();
                patientDto.setId(lblID.getText());
                patientDto.setTitle(cmbTitle.getValue());
                patientDto.setName(txtName.getText());
                patientDto.setDob(dpDOB.getValue());
                patientDto.setHomeAddress(txtHomeAddress.getText());
                patientDto.setTelephone(txtPhoneNumber.getText());
                patientDto.setEmailAddress(txtEmail.getText());

                try {
                    boolean isUpdate = patientBo.updatePatient(patientDto);
                    if (isUpdate) {
                        patientTableFormController.loadPatientTable();
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Patient updated successfully!", true);
                        Stage stage = (Stage) btnAction.getScene().getWindow();
                        stage.close();

                    }else {
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Can't update Patient", false);
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
    void txtEmailOnKeyTyped(KeyEvent event) {

        if (txtEmail.getText().isEmpty()) {
            lblValidateEmail.setText("");
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
    void txtNameOnKeyTyped(KeyEvent event) {
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
    void txtPhoneNumberOnKeyTyped(KeyEvent event) {
        if (txtPhoneNumber.getText().isEmpty()) {
            lblValidatePhoneNumber.setText("");
            return;
        }
        if (!txtPhoneNumber.getText().matches(RegexUtil.phoneNumberRegex)) {
            lblValidatePhoneNumber.setText("Invalid phone number format");
            RegexUtil.setErrorStyle(false,txtPhoneNumber);

        }else {
            lblValidatePhoneNumber.setText("");
            RegexUtil.resetErrorStyle(txtPhoneNumber);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            lblID.setText(patientBo.generateNewPatientID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObservableList<String> titles = FXCollections.observableArrayList(
                "Mr", "Ms", "Mrs", "Rev"
        );
        cmbTitle.setItems(titles);
    }

    private void resetFields(){
        txtName.clear();
        txtEmail.clear();
        txtPhoneNumber.clear();
        txtHomeAddress.clear();
        txtPhoneNumber.clear();
        cmbTitle.getSelectionModel().clearSelection();
        dpDOB.getEditor().clear();
        lblValidateName.setText("");
        lblValidateEmail.setText("");
        lblValidatePhoneNumber.setText("");
    }

    public boolean isFillAllFields() {

        return !txtName.getText().isEmpty() &&
                !txtEmail.getText().isEmpty() &&
                !txtPhoneNumber.getText().isEmpty() &&
                !txtHomeAddress.getText().isEmpty() &&
                cmbTitle.getSelectionModel().getSelectedItem() != null &&
                dpDOB.getValue() != null;
    }

    public void setPatientActionBtnText(String text) {
        btnAction.setText(text);
    }

    public void setLblTitle(String title) {
        lblTitle.setText(title);
    }

    public void setPatientData(PatientDto patientDto) {
        lblID.setText(patientDto.getId());
        txtName.setText(patientDto.getName());
        Platform.runLater(() -> {
            txtName.positionCaret(txtName.getText().length());
        });
        cmbTitle.getSelectionModel().select(patientDto.getTitle());
        dpDOB.setValue(patientDto.getDob());
        txtPhoneNumber.setText(patientDto.getTelephone());
        txtEmail.setText(patientDto.getEmailAddress());
        txtHomeAddress.setText(patientDto.getHomeAddress());
    }
}
