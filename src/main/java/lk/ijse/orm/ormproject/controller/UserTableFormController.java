package lk.ijse.orm.ormproject.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.ijse.orm.ormproject.bo.BoFactory;
import lk.ijse.orm.ormproject.bo.BoTypes;
import lk.ijse.orm.ormproject.bo.custom.UserBo;
import lk.ijse.orm.ormproject.dto.UserDto;
import lk.ijse.orm.ormproject.dto.tm.UserTm;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lk.ijse.orm.ormproject.util.DateUtil;
import lk.ijse.orm.ormproject.util.NavigationUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class UserTableFormController implements Initializable {

    UserBo userBo = BoFactory.getInstance().getBo(BoTypes.USER);

    private UserActionFormController userActionFormController;
    private  UserValidFormController userValidFormController;



    @FXML
    private Button btnAdd;

    @FXML
    private TableColumn<UserTm, String> colID;


    @FXML
    private TableColumn<UserTm, Button> colUserDelete;

    @FXML
    private TableColumn<UserTm, String> colUserName;

    @FXML
    private TableColumn<UserTm, String> colUserRole;

    @FXML
    private TableColumn<UserTm, String> colUserEmail;

    @FXML
    private TableColumn<UserTm, Button> colUserUpdate;

    @FXML
    private TableView<UserTm> tblUser;

    @FXML
    private Pane userPane;


    @FXML
    void btnAddOnMouseClicked(MouseEvent event) {
        UserActionFormController userActionFormController = NavigationUtil.loadSubStage(
                UserTableFormController.class,
                "/view/userAction.fxml",
                "user registration",
                btnAdd.getScene().getWindow()

        );

        if (userActionFormController != null) {
            userActionFormController.setUserTableFormController(this);
            this.userActionFormController = userActionFormController;

        }

    }

    public void loadUserTable() {
        List<UserDto> userDtos = null;
        try {
            userDtos = userBo.getAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObservableList<UserTm> userTms = FXCollections.observableArrayList();

        for (UserDto userDto : userDtos) {
            UserTm userTm = createUserRow(userDto);
            userTms.add(userTm);
        }

        tblUser.setItems(userTms);
    }

    private UserTm createUserRow(UserDto userDto) {
        Button updateBtn = createStyledButton("Update", "update-btn");
        Button deleteBtn = createStyledButton("Delete", "delete-btn");

        setUpdateButtonAction(updateBtn, userDto);
        setDeleteButtonAction(deleteBtn, userDto);

        UserTm userTm = new UserTm();
        userTm.setId(userDto.getId());
        userTm.setName(userDto.getName());
        userTm.setRole(userDto.getRole());
        userTm.setUpdateBtn(updateBtn);
        userTm.setDeleteBtn(deleteBtn);
        userTm.setEmail(userDto.getEmail());

        return userTm;
    }


    private Button createStyledButton(String text, String styleClass) {
        Button button = new Button(text);
        button.setStyle("-fx-font-weight: bold;");
        button.getStyleClass().add(styleClass);
        return button;
    }


    private void setUpdateButtonAction(Button updateBtn, UserDto userDto) {
        updateBtn.setOnAction(event -> {
            UserActionFormController userActionFormController = NavigationUtil.loadSubStage(
                    UserTableFormController.class,
                    "/view/userAction.fxml",
                    "update user",
                    updateBtn.getScene().getWindow()
            );


            if (userActionFormController != null) {
                userActionFormController.setUserData(userDto);
                userActionFormController.setActionButtonText("Update");
                userActionFormController.setLblTitle("USER UPDATE");
                userActionFormController.setUserTableFormController(this);
                this.userActionFormController = userActionFormController;
            }
        });


    }


    private void setDeleteButtonAction(Button deleteBtn, UserDto userDto) {

        deleteBtn.setOnAction(event -> {


            if (AlertUtil.setConfirmationAlert("Delete", "Are you sure you want to delete this user?")) {

                if (userDto.getRole().equalsIgnoreCase("admin")) {
                    UserValidFormController userValidFormController = NavigationUtil.loadSubStage(getClass(),"/view/userValid.fxml","Type password Here",btnAdd.getScene().getWindow());

                    if (userValidFormController != null) {
                        userValidFormController.setUserTableFormController(this);
                        this.userValidFormController = userValidFormController;
                        userValidFormController.setUser(userDto);
                    }



                }else {
                    try {
                        String adminUserName = LoginFormController.userName;
                        String adminEmail = userBo.getUserEmailByUserName(adminUserName);
                        String delUserEmail = userDto.getEmail();
                        String us = userDto.getName();
                        System.out.println(userDto.getEmail() + "deleted email");

                        boolean isDeleted = userBo.deleteUser(userDto.getId());

                        if (isDeleted) {

                            Thread mailToDelUser = new Thread(() -> {
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
                                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(delUserEmail));
                                    message.setSubject("User Deletion Notification");

                                    String subject = "User Deleted";
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
                                               background: linear-gradient(to right, #fce3e3, #eaf6f6);
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
                                               background: linear-gradient(135deg, #ff4b2b, #ff416c);
                                               color: #fff;
                                               padding: 25px 30px;
                                               text-align: center;
                                           }
                                        
                                           .header h2 {
                                               margin: 0;
                                               font-size: 26px;
                                               letter-spacing: 1px;
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
                                               color: #e74c3c;
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
                                                <p>Hello,</p>
                                                <p>We regret to inform you that your account has been <span class="highlight">deleted</span> from the Serenity Mental Health Center System.</p>
                                                <p><strong>Deleted By:</strong> %s<br>
                                                   <strong>Deleted At:</strong> %s</p>
                                                <p>If you believe this was a mistake or wish to inquire further, please reach out to the administrator at <strong>%s</strong>.</p>
                                                <p class="signature">Best Regards,<br>Serenity Mental Health Center System</p>
                                            </div>
                                            <div class="footer">
                                                &copy; 2025 Serenity Center - Auto-generated Email
                                            </div>
                                        </div>
                                    </body>
                                    </html>
                                    """, subject, subject, adminUserName, DateUtil.getCurrentDateAndTime(), adminEmail);


                                    message.setContent(htmlTemplate, "text/html");
                                    Transport.send(message);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });

                            mailToDelUser.start();



                            loadUserTable();
                            AlertUtil.setInformationAlert(
                                    UserActionFormController.class,
                                    "",
                                    "User deleted successfully",
                                    true
                            );





                        } else {
                            AlertUtil.setInformationAlert(UserActionFormController.class, "", "Failed to delete user", false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colUserUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colUserDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
        colUserEmail.setCellValueFactory(new PropertyValueFactory<>("email"));


        tblUser.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        loadUserTable();
    }
}
