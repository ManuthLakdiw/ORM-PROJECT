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
import lk.ijse.orm.ormproject.bo.custom.UserBo;
import lk.ijse.orm.ormproject.dto.UserDto;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class userActionFormController implements Initializable {

    UserBo userBo = BoFactory.getInstance().getBo(BoTypes.USER);

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

                        UserDto dto = new UserDto();
                        dto.setId(lblID.getText());
                        dto.setName(txtUserName.getText());
                        dto.setPassword(txtPassword.getText());
                        dto.setRole(cmbRole.getValue());

                        boolean isSaved = userBo.saveUser(dto);
                        if (isSaved) {
                            AlertUtil.setInformationAlert(userActionFormController.class, "", "User saved successfully!", true);
                            refreshFields();
                            userTableFormController.loadUserTable();
                        } else {
                            AlertUtil.setInformationAlert(userActionFormController.class, "", "Can't save user", false);
                        }
                    }
                } catch (Exception e) {
                    AlertUtil.setInformationAlert(userActionFormController.class, "", e.getMessage(), false);
                }
            } else {
                AlertUtil.setInformationAlert(userActionFormController.class, "", "Please fill all the fields!", true);
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

                        if (txtPassword.getText() != null && txtConfirmPassword.getText() != null &&
                                !txtPassword.getText().equals(txtConfirmPassword.getText())) {
                            AlertUtil.setInformationAlert(getClass(),"","Can't update!\nPassword do not match!",false);
                            return;
                        }


                        if (txtPassword != null && txtPassword.getText() != null && !txtPassword.getText().isEmpty()) {
                            dto.setPassword(txtPassword.getText());
                        }

                        boolean isUpdated = userBo.updateUser(dto);
                        if (isUpdated) {
                            refreshFields();
                            userTableFormController.loadUserTable();
                            AlertUtil.setInformationAlert(userActionFormController.class, "", "User updated successfully!", true);
                            Stage stage = (Stage) btnAction.getScene().getWindow();
                            stage.close();

                        } else {
                            AlertUtil.setInformationAlert(userActionFormController.class, "", "Can't update user", false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertUtil.setInformationAlert(userActionFormController.class, "", e.getMessage(), false);
                }
            } else {
                AlertUtil.setInformationAlert(userActionFormController.class, "", "Please fill all the fields!", true);
            }
        }
    }


    @FXML
    void txtConfirmPasswordOnKeyTyped(KeyEvent event) {

        if (txtConfirmPassword.getText().isEmpty()) {
            lblConfirmPassword.setText("");
            return;
        }

        if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
            lblConfirmPassword.setText("Passwords do not match ‼️");
        } else {
            lblConfirmPassword.setText("");
        }
    }

    @FXML
    void txtPasswordOnKeyTyped(KeyEvent event) {

        if (txtPassword.getText().isEmpty()) {
            lblPassword.setText("");
            txtConfirmPassword.setDisable(false);
            return;
        }
        if (!txtPassword.getText().matches(passwordRegex)) {
            lblPassword.setText("Password should be Strong ‼️");
            txtConfirmPassword.setText("");
            txtConfirmPassword.setDisable(true);
        } else {
            lblPassword.setText("");
            txtConfirmPassword.setDisable(false);

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
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
                cmbRole.getSelectionModel().getSelectedItem() != null;
    }

    public boolean isAllFieldHasValueForUpdate() {
        return !txtUserName.getText().isEmpty() &&
                cmbRole.getSelectionModel().getSelectedItem() != null;
    }

    public void refreshFields(){
        try {
            lblID.setText(userBo.generateNewID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtUserName.clear();
        txtPassword.clear();
        txtConfirmPassword.clear();
        cmbRole.getSelectionModel().clearSelection();
        lblConfirmPassword.setText("");
        lblPassword.setText("");
        Platform.runLater(() -> txtUserName.requestFocus());

    }

    public void setUserData(UserDto userDto) {
        lblID.setText(userDto.getId());
        txtUserName.setText(userDto.getName());
        txtPassword.setText(userDto.getPassword());
        txtConfirmPassword.setText(userDto.getPassword());
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
