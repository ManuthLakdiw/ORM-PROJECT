package lk.ijse.orm.ormproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lk.ijse.orm.ormproject.bo.BoFactory;
import lk.ijse.orm.ormproject.bo.BoTypes;
import lk.ijse.orm.ormproject.bo.custom.UserBo;
import lk.ijse.orm.ormproject.dto.UserDto;
import lk.ijse.orm.ormproject.dto.tm.UserTm;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lk.ijse.orm.ormproject.util.NavigationUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserTableFormController implements Initializable {

    UserBo userBo = BoFactory.getInstance().getBo(BoTypes.USER);

    private UserActionFormController userActionFormController;


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
                try {
                    boolean isDeleted = userBo.deleteUser(userDto.getId());
                    if (isDeleted) {
                        loadUserTable();
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "User deleted successfully", true);
                    } else {
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Failed to delete user", false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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


        tblUser.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        loadUserTable();
    }
}
