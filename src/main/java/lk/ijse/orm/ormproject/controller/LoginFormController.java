package lk.ijse.orm.ormproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.orm.ormproject.bo.BoFactory;
import lk.ijse.orm.ormproject.bo.BoTypes;
import lk.ijse.orm.ormproject.bo.custom.UserBo;
import lk.ijse.orm.ormproject.exception.InvalidCredentialException;
import lk.ijse.orm.ormproject.exception.MissingFieldException;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lk.ijse.orm.ormproject.util.NavigationUtil;
import javafx.scene.layout.AnchorPane;
import lk.ijse.orm.ormproject.util.RegexUtil;


import java.net.URL;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {





    @FXML
    private Button btnLogin;

    @FXML
    private CheckBox checkBox;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtShowPw;

    @FXML
    private AnchorPane logPane;

    @FXML
    private TextField txtUserName;


    UserBo user = BoFactory.getInstance().getBo(BoTypes.USER);


    private DashBoardFormController dashboardFormController;

    public static String userName;


    @FXML
    void btnLoginOnMouseClicked(MouseEvent event) throws Exception {

//        NavigationUtil.getNewStage(
//                (Stage) logPane.getScene().getWindow(),
//                LoginFormController.class
//                , "Dashboard"
//                , "/view/dashBoard.fxml"
//        );


        if (!(txtUserName.getText().isEmpty() || txtPassword.getText().isEmpty())) {
            boolean isVerified = user.verifyUser(txtUserName.getText(), txtShowPw.getText());
            if (isVerified) {
//                AlertUtil.setInformationAlert(LoginFormController.class , "" , "Verified" , true);
                userName = txtUserName.getText();
                String userRole = user.getUserRoleByName(txtUserName.getText());


                NavigationUtil.getNewStage(
                        (Stage) logPane.getScene().getWindow(),
                        LoginFormController.class
                        , "Dashboard"
                        , "/view/dashBoard.fxml"
                );

                DashBoardFormController dashBoardFormController = NavigationUtil.getController();

                if (dashBoardFormController != null) {
                    if (userRole.equalsIgnoreCase("Receptionist")) {
                        dashBoardFormController.manageRoleTask(false);

                    }
                    dashBoardFormController.setLoginFormController(this);
                    this.dashboardFormController = dashBoardFormController;

                }

            }else {
                AlertUtil.setInformationAlert(LoginFormController.class , "" , new InvalidCredentialException("Invalid username or password. Please try again.").getMessage(), false);
                RegexUtil.setErrorStyle(true ,txtPassword,txtUserName,txtShowPw);
                txtPassword.clear();
                txtUserName.clear();
                txtShowPw.clear();

            }
        }else {
            AlertUtil.setInformationAlert(LoginFormController.class , "" , new MissingFieldException("Please enter both username and password to continue").getMessage(), true);
        }


//
//        UserDto dto = new UserDto();
//        dto.setId("U001");
//        dto.setName("ma");
//        dto.setPassword("12");
//        dto.setRole("admin");
//        boolean check = user.saveUser(dto);
//        System.out.println(check);


    }

    @FXML
    void checkBoxOnMouseClicked(MouseEvent event) {
        if (checkBox.isSelected()) {
            txtShowPw.setText(txtPassword.getText());
            txtShowPw.setVisible(true);
            txtPassword.setVisible(false);
        } else {
            txtPassword.setText(txtShowPw.getText());
            txtShowPw.setVisible(false);
            txtPassword.setVisible(true);
        }
    }

    @FXML
    void txtPasswordOnKeyTyped(KeyEvent event) {
        txtShowPw.setText(txtPassword.getText());
        resetLoginFields(txtPassword , txtUserName , txtShowPw);

    }

    @FXML
    void txtShowPwOnKeyTyped(KeyEvent event) {
        txtPassword.setText(txtShowPw.getText());
        resetLoginFields(txtPassword , txtUserName , txtShowPw);

    }

    @FXML
    void txtUserNameOnKeyTyped(KeyEvent event) {
        resetLoginFields(txtPassword , txtUserName , txtShowPw);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtShowPw.setVisible(false);
    }


    private <T extends TextInputControl> void resetLoginFields(T... inputNodes) {
        String resetStyle = "-fx-border-color: white;";
        for (T node : inputNodes) {
            node.setStyle(resetStyle);
        }

    }
}
