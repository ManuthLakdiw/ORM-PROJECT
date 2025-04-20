package lk.ijse.orm.ormproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lk.ijse.orm.ormproject.util.NavigationUtil;
import lombok.Getter;
import lombok.Setter;

public class SessionTableFormController {

    @Getter
    @Setter
    private SessionActionFormController sessionActionFormController;

    @FXML
    private Button btnAdd;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDelete;

    @FXML
    private TableColumn<?, ?> colEndTime;

    @FXML
    private TableColumn<?, ?> colID;

    @FXML
    private TableColumn<?, ?> colProgrammeName;

    @FXML
    private TableColumn<?, ?> colStartTime;

    @FXML
    private TableColumn<?, ?> colTherapistName;

    @FXML
    private TableColumn<?, ?> colUpdate;

    @FXML
    private Pane sessionPane;

    @FXML
    private TableView<?> tblSession;

    @FXML
    void btnAddOnMouseClicked(MouseEvent event) {
        SessionActionFormController sessionActionFormController = NavigationUtil.loadSubStage(
                SessionTableFormController.class,
                "/view/sessionAction.fxml",
                "Session scheduling",
                btnAdd.getScene().getWindow()

        );

        if (sessionActionFormController != null) {
            sessionActionFormController.setSessionTableFormController(this);
            this.sessionActionFormController = sessionActionFormController;

        }

    }

}
