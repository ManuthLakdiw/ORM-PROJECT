package lk.ijse.orm.ormproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.orm.ormproject.bo.BoFactory;
import lk.ijse.orm.ormproject.bo.BoTypes;
import lk.ijse.orm.ormproject.bo.custom.UserBo;
import lk.ijse.orm.ormproject.dto.UserDto;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lk.ijse.orm.ormproject.util.NavigationUtil;
import lombok.Getter;
import lombok.Setter;

public class UserValidFormController{

    public static boolean check = false;

    @Getter
    @Setter
    private UserTableFormController userTableFormController ;

    @Getter
    @Setter
    private UserActionFormController userActionFormController ;

    UserBo userBo = BoFactory.getInstance().getBo(BoTypes.USER);


    @FXML
    private PasswordField txtPassword;

    @FXML
    private AnchorPane userValidPane;

    String userName;
    String id;
    String email;
    String role;
    String password;

    @Getter
    @Setter
    private boolean isRemove = false;

    @Getter
    @Setter
    private boolean isEdit = false;


    @FXML
    void txtPasswordOnKeyTyped(KeyEvent event) {
        try {
            if (isRemove) {
                boolean isValid = userBo.verifyUser(userName, txtPassword.getText());
                if (isValid) {
                    Stage stage = (Stage) userValidPane.getScene().getWindow();
                    stage.close();
                    if (AlertUtil.setConfirmationAlert("Confirm Message", "Admin Will Remove!!!")) {
                        boolean isDeleted = userBo.deleteUser(id);
                        if (isDeleted) {
                            userTableFormController.loadUserTable();
                            AlertUtil.setInformationAlert(UserActionFormController.class, "", "User deleted successfully", true);
                        } else {
                            AlertUtil.setInformationAlert(UserActionFormController.class, "", "Failed to delete user", false);
                        }
                    }
                }
            }

            if (isEdit) {
                boolean isValid = userBo.verifyUser(userName, txtPassword.getText());
                if (isValid) {
                    Stage stage = (Stage) userValidPane.getScene().getWindow();
                    stage.close();
                    UserActionFormController userActionFormController = NavigationUtil.loadSubStage(
                            UserTableFormController.class,
                            "/view/userAction.fxml",
                            "update user",
                            txtPassword.getScene().getWindow()
                    );


                    if (userActionFormController != null) {
                        UserDto userDto = new UserDto();
                        userDto.setId(id);
                        userDto.setName(userName);
                        userDto.setPassword(password);
                        userDto.setEmail(email);
                        userDto.setRole(role);

                        userActionFormController.setUserData(userDto);
                        userActionFormController.setActionButtonText("Update");
                        userActionFormController.setLblTitle("USER UPDATE");
                        userActionFormController.setUserValidFormController(this);
                        userActionFormController.setUserTableFormController(getUserTableFormController());
                        userActionFormController.setSendEmail(false);
                        this.userActionFormController = userActionFormController;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setUser(UserDto userDto) {
        userName = userDto.getName();
        id = userDto.getId();
        email = userDto.getEmail();
        role = userDto.getRole();
        password = userDto.getPassword();

    }


}
