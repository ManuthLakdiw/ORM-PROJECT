package lk.ijse.orm.ormproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.orm.ormproject.bo.BoFactory;
import lk.ijse.orm.ormproject.bo.BoTypes;
import lk.ijse.orm.ormproject.bo.custom.UserBo;

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
    private TextField txtUserName;


    UserBo user = BoFactory.getInstance().getBo(BoTypes.USER);


    private String userName;
    private String password;



    @FXML
    void btnLoginOnMouseClicked(MouseEvent event) {
        userName = txtUserName.getText();
        password = txtShowPw.getText();

        boolean isVerified = user.verifyUser(userName, password);





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
    }

    @FXML
    void txtShowPwOnKeyTyped(KeyEvent event) {
        txtPassword.setText(txtShowPw.getText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtShowPw.setVisible(false);
    }
}
