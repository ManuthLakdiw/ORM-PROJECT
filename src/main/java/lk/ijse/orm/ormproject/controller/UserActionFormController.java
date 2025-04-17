package lk.ijse.orm.ormproject.controller;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.orm.ormproject.bo.BoFactory;
import lk.ijse.orm.ormproject.bo.BoTypes;
import lk.ijse.orm.ormproject.bo.custom.UserBo;
import lk.ijse.orm.ormproject.exception.MissingFieldException;
import lk.ijse.orm.ormproject.dto.UserDto;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lk.ijse.orm.ormproject.util.DateUtil;
import lk.ijse.orm.ormproject.util.RegexUtil;
import lombok.Setter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class UserActionFormController implements Initializable {

    UserBo userBo = BoFactory.getInstance().getBo(BoTypes.USER);
    String oldEmailAddress;
    String newPassword;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnAction;

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private Label lblConfirmPassword;

    @FXML
    private Label lblID;


    @FXML
    private Label lblEmail;


    @FXML
    private TextField txtEmail;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserName;


    private String passwordRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}$";
    private String emailRegex = "[\\w]*@*[a-z]*\\.*[\\w]{5,}(\\.)*(com)*(@gmail\\.com)";



    @Setter
    private UserTableFormController userTableFormController;


    @FXML
    void btnClearOnMouseClicked(MouseEvent event) {
        refreshFields();
    }

    @FXML
    void btnActionOnMouseClicked(MouseEvent event) {
        if (btnAction.getText().equalsIgnoreCase("Save")) {
            if (isAllFieldHasValueForSave()) {
                try {
                    boolean isExistsUserName = userBo.validUserName(txtUserName.getText());

                    if (isExistsUserName) {

                        if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
                            AlertUtil.setInformationAlert(getClass(),"","Can't save!\nPassword do not match!",false);
                            return;
                        }

                        if (!txtEmail.getText().matches(emailRegex)) {
                            AlertUtil.setInformationAlert(getClass(),"","Can't save!\nEmail is not valid!",false);
                            return;
                        }

                        UserDto dto = new UserDto();
                        dto.setId(lblID.getText());
                        dto.setName(txtUserName.getText());
                        dto.setPassword(txtPassword.getText());
                        dto.setRole(cmbRole.getValue());
                        dto.setEmail(txtEmail.getText());

                        boolean isSaved = userBo.saveUser(dto);
                        if (isSaved) {
                            AlertUtil.setInformationAlert(UserActionFormController.class, "", "User saved successfully!", true);
                            refreshFields();
                            userTableFormController.loadUserTable();
                        } else {
                            AlertUtil.setInformationAlert(UserActionFormController.class, "", "Can't save user", false);
                        }
                    }
                } catch (Exception e) {
                    AlertUtil.setInformationAlert(UserActionFormController.class, "", e.getMessage(), false);
                }
            } else {
                AlertUtil.setInformationAlert(
                        UserActionFormController.class,
                        "",
                        new MissingFieldException("Required field is missing. Please provide all necessary information.").getMessage(),
                        true
                );
            }
        }

        if (btnAction.getText().equalsIgnoreCase("Update")) {
            if (isAllFieldHasValueForUpdate()) {
                try {
                    if (txtUserName.getText().equals(userBo.getUserNameById(lblID.getText())) || userBo.validUserName(txtUserName.getText()) ) {
                        UserDto dto = new UserDto();
                        dto.setId(lblID.getText());
                        dto.setName(txtUserName.getText());
                        dto.setRole(cmbRole.getValue());
                        dto.setEmail(txtEmail.getText());

                        if (txtPassword.getText() != null && txtConfirmPassword.getText() != null &&
                                !txtPassword.getText().equals(txtConfirmPassword.getText())) {
                            AlertUtil.setInformationAlert(getClass(),"","Can't update!\nPassword do not match!",false);
                            return;
                        }

                        if (!txtEmail.getText().matches(emailRegex)) {
                            AlertUtil.setInformationAlert(getClass(),"","Can't update!\nEmail is not valid!",false);
                            return;

                        }


                        if (txtPassword != null && txtPassword.getText() != null && !txtPassword.getText().isEmpty()) {
                            dto.setPassword(txtPassword.getText());
                            newPassword = dto.getPassword();
                        }
                        boolean isUpdated = userBo.updateUser(dto);
                        String newUserName = dto.getName();
                        String newEmail = dto.getEmail();
                        String newRole = dto.getRole();
                        if (newPassword == null) {
                            newPassword = "Password doesn't changed";
                        }

                        String adminUserName = LoginFormController.userName;
                        String adminEmail = userBo.getUserEmailByUserName(adminUserName);
                        if (isUpdated) {
                            new Thread(() -> {
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
                                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(oldEmailAddress));
                                    message.setSubject("User Update Notification");

                                    String subject = "User Updated";
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
                background: linear-gradient(to right, #dbeafe, #f0fdfa);
                margin: 0;
                padding: 30px 0;
                color: #333;
            }

            .container {
                max-width: 650px;
                margin: auto;
                background-color: #ffffff;
                border-radius: 15px;
                box-shadow: 0 5px 25px rgba(0, 0, 0, 0.1);
                animation: fadeIn 0.8s ease-in-out;
                overflow: hidden;
            }

            .header {
                background: linear-gradient(135deg, #3b82f6, #06b6d4);
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
                font-size: 16px;
                line-height: 1.7;
            }

            .content p {
                margin-bottom: 20px;
            }

            .label {
                font-weight: bold;
                color: #0f172a;
            }

            .value {
                color: #1e293b;
            }

            .password-box {
                background-color: #f1f5f9;
                padding: 10px 15px;
                border-radius: 8px;
                width: fit-content;
                margin-top: 10px;
                cursor: pointer;
                user-select: none;
                font-family: monospace;
                display: inline-block;
                transition: background-color 0.3s ease;
            }

            .password-box:hover {
                background-color: #e2e8f0;
            }

            .footer {
                background-color: #f8fafc;
                padding: 20px;
                text-align: center;
                font-size: 13px;
                color: #94a3b8;
            }

            .signature {
                margin-top: 25px;
                font-style: italic;
                color: #475569;
            }
        
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h2>%s</h2>
            </div>
            <div class="content">
                <p>Hello,</p>
                <p>Your account details have been <span class="label">successfully updated</span> in the Serenity Mental Health Center System.</p>

                <p><span class="label">Updated By:</span> %s<br>
                   <span class="label">Updated At:</span> %s</p>

                <p class="label">Your New Credentials:</p>
                <p><span class="label">Name:</span> <span class="value">%s</span><br>
                   <span class="label">Email:</span> <span class="value">%s</span><br>
                   <span class="label">Role:</span> <span class="value">%s</span></p>

                <p><span class="label">Password:</span></p>
                <label class="password-box">
                    <span class="password-label">%s</span>
                </label>
                <p>If you believe this was a mistake or wish to inquire further, please reach out to the administrator at <strong>%s</strong>.</p>
                <p class="signature">Best Regards,<br>Serenity Mental Health Center System</p>
            </div>
            <div class="footer">
                &copy; 2025 Serenity Center - Auto-generated Email
            </div>
        </div>
   
    </body>
    </html>
    """, subject, subject, adminUserName, DateUtil.getCurrentDateAndTime(), newUserName, newEmail, newRole, newPassword,adminEmail);


                                    message.setContent(htmlTemplate, "text/html");
                                    Transport.send(message);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }).start();
                            refreshFields();
                            userTableFormController.loadUserTable();
                            AlertUtil.setInformationAlert(UserActionFormController.class, "", "User updated successfully!", true);
                            Stage stage = (Stage) btnAction.getScene().getWindow();
                            stage.close();



                        } else {
                            AlertUtil.setInformationAlert(UserActionFormController.class, "", "Can't update user", false);
                        }
                    }
                } catch (Exception e) {
                    AlertUtil.setInformationAlert(UserActionFormController.class, "", e.getMessage(), false);
                }
            } else {
                AlertUtil.setInformationAlert(
                        UserActionFormController.class,
                        "",
                        new MissingFieldException("Required field is missing.\nPlease provide all necessary information.").getMessage(),
                        true
                );
            }
        }
    }


    @FXML
    void txtConfirmPasswordOnKeyTyped(KeyEvent event) {

        if (txtConfirmPassword.getText().isEmpty()) {
            lblConfirmPassword.setText("");
            RegexUtil.resetErrorStyle(txtConfirmPassword);
            return;
        }

        if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
            lblConfirmPassword.setText("Passwords do not match ‼️");
            RegexUtil.setErrorStyle(false,txtConfirmPassword);
        } else {
            lblConfirmPassword.setText("");
            RegexUtil.resetErrorStyle(txtConfirmPassword);
        }
    }

    @FXML
    void txtPasswordOnKeyTyped(KeyEvent event) {

        if (txtPassword.getText().isEmpty()) {
            lblPassword.setText("");
            txtConfirmPassword.setDisable(false);
            RegexUtil.resetErrorStyle(txtPassword);
            return;
        }
        if (!txtPassword.getText().matches(passwordRegex)) {
            lblPassword.setText("Password should be Strong ‼️");
            RegexUtil.setErrorStyle(false,txtPassword);
            txtConfirmPassword.setText("");
            RegexUtil.resetErrorStyle(txtConfirmPassword);
            txtConfirmPassword.setDisable(true);
        } else {
            lblPassword.setText("");
            txtConfirmPassword.setDisable(false);
            RegexUtil.resetErrorStyle(txtPassword);

        }

    }

    @FXML
    void txtEmailOnKeyTyped(KeyEvent event) {
        if (txtEmail.getText().isEmpty()) {
            lblEmail.setText("");
            RegexUtil.resetErrorStyle(txtEmail);
            return;
        }
        if (!txtEmail.getText().matches(emailRegex)) {
            lblEmail.setText("Invalid email format ‼️");
            RegexUtil.setErrorStyle(false,txtEmail);
        } else {
            lblEmail.setText("");
            RegexUtil.resetErrorStyle(txtEmail);
        }


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            System.out.println("call initialize"+ oldEmailAddress);
            lblID.setText(userBo.generateNewID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObservableList<String> roles = FXCollections.observableArrayList("Admin","Receptionist");
        cmbRole.setItems(roles);
        Platform.runLater(() -> txtUserName.requestFocus());
        txtUserName.positionCaret(txtUserName.getText().length());
    }

    public boolean isAllFieldHasValueForSave(){
        return !txtUserName.getText().isEmpty() &&
                !txtPassword.getText().isEmpty() &&
                !txtConfirmPassword.getText().isEmpty() &&
                !txtEmail.getText().isEmpty() &&
                cmbRole.getSelectionModel().getSelectedItem() != null;
    }

    public boolean isAllFieldHasValueForUpdate() {
        return !txtUserName.getText().isEmpty() &&
                !txtEmail.getText().isEmpty() &&
                cmbRole.getSelectionModel().getSelectedItem() != null;
    }

    public void refreshFields(){
        try {
            lblID.setText(userBo.generateNewID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RegexUtil.resetErrorStyle(txtUserName, txtPassword, txtConfirmPassword, txtEmail);
        txtUserName.clear();
        txtPassword.clear();
        txtConfirmPassword.clear();
        txtEmail.clear();
        cmbRole.getSelectionModel().clearSelection();
        lblConfirmPassword.setText("");
        lblPassword.setText("");
        lblEmail.setText("");
        Platform.runLater(() -> txtUserName.requestFocus());

    }

    public void setUserData(UserDto userDto) {
        lblID.setText(userDto.getId());
        txtUserName.setText(userDto.getName());
        txtPassword.setText(userDto.getPassword());
        txtConfirmPassword.setText(userDto.getPassword());
        txtEmail.setText(userDto.getEmail());
        oldEmailAddress = userDto.getEmail();
        Platform.runLater(() -> {
            txtUserName.positionCaret(txtUserName.getText().length());
        });
        cmbRole.setValue(userDto.getRole());
    }

    public void setActionButtonText(String text) {
        btnAction.setText(text);
    }

    public void setLblTitle(String title) {
        lblTitle.setText(title);

    }


}
