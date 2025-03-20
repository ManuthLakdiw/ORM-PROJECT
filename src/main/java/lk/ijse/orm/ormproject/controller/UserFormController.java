package lk.ijse.orm.ormproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lk.ijse.orm.ormproject.bo.BoFactory;
import lk.ijse.orm.ormproject.bo.BoTypes;
import lk.ijse.orm.ormproject.bo.custom.UserBo;
import lk.ijse.orm.ormproject.dto.UserDto;
import lk.ijse.orm.ormproject.dto.tm.UserTm;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lk.ijse.orm.ormproject.util.NavigationUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserFormController implements Initializable {

    UserBo userBo = BoFactory.getInstance().getBo(BoTypes.USER);

    private AddUserFormController addUserFormController;


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
        AddUserFormController addUserFormController = NavigationUtil.loadSubStage(
                UserFormController.class,
                "/view/addUser.fxml",
                "user registration",
                btnAdd.getScene().getWindow()

        );

        if (addUserFormController != null) {
            addUserFormController.setUserFormController(this);
            this.addUserFormController = addUserFormController;

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
            AddUserFormController addUserFormController = NavigationUtil.loadSubStage(
                    UserFormController.class,
                    "/view/addUser.fxml",
                    "update user",
                    updateBtn.getScene().getWindow()
            );


            if (addUserFormController != null) {
                addUserFormController.setUserData(userDto);
                addUserFormController.setActionButtonText("Update");
                addUserFormController.setLblTitle("USER UPDATE");
                addUserFormController.setUserFormController(this);
                this.addUserFormController = addUserFormController;
            }
        });


    }



    private void setDeleteButtonAction(Button deleteBtn, UserDto userDto) {
        deleteBtn.setOnAction(event -> {
            if (AlertUtil.setConfirmationAlert("Delete", "Are you sure you want to delete this user?")) {
                try {
                    boolean isDeleted = userBo.deleteUser(userDto.getId());
                    if (isDeleted) {
                        for (int i = 0; i < tblUser.getItems().size(); i++) {
                            if (tblUser.getItems().get(i).getId().equals(userDto.getId())) {
                                tblUser.getItems().remove(i);
                                break;
                            }
                        }
                        AlertUtil.setInformationAlert(AddUserFormController.class, "", "User deleted successfully", true);
                    } else {
                        AlertUtil.setInformationAlert(AddUserFormController.class, "", "Failed to delete user", false);
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
