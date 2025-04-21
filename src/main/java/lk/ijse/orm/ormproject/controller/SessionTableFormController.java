package lk.ijse.orm.ormproject.controller;

import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lk.ijse.orm.ormproject.bo.BoFactory;
import lk.ijse.orm.ormproject.bo.BoTypes;
import lk.ijse.orm.ormproject.bo.custom.TherapySessionBo;
import lk.ijse.orm.ormproject.dto.TherapySessionDto;
import lk.ijse.orm.ormproject.dto.tm.TherapySessionTm;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lk.ijse.orm.ormproject.util.NavigationUtil;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class SessionTableFormController implements Initializable {

    @Getter
    @Setter
    private SessionActionFormController sessionActionFormController;

    TherapySessionBo therapySessionBo = BoFactory.getInstance().getBo(BoTypes.THERAPYSESSION);

    @FXML
    private Button btnAdd;

    @FXML
    private TableColumn<TherapySessionTm, LocalDate> colDate;

    @FXML
    private TableColumn<TherapySessionTm, Button> colDelete;

    @FXML
    private TableColumn<TherapySessionTm, LocalTime> colEndTime;

    @FXML
    private TableColumn<TherapySessionTm, String> colID;

    @FXML
    private TableColumn<TherapySessionTm, String> colProgrammeName;

    @FXML
    private TableColumn<TherapySessionTm, LocalTime> colStartTime;

    @FXML
    private TableColumn<TherapySessionTm, String> colTherapistName;

    @FXML
    private TableColumn<TherapySessionTm, Button> colUpdate;

    @FXML
    private Pane sessionPane;

    @FXML
    private TableView<TherapySessionTm> tblSession;

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

    public void loadSessionTable() {
        try {
            List<TherapySessionDto> allTherapySessions = therapySessionBo.getAllTherapySessions();
            ObservableList<TherapySessionTm> therapySessionTms = FXCollections.observableArrayList();
            for (TherapySessionDto therapySessionDto : allTherapySessions) {
                TherapySessionTm therapySessionTm = createTableRow(therapySessionDto);
                therapySessionTms.add(therapySessionTm);
            }
            tblSession.setItems(therapySessionTms);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private TherapySessionTm createTableRow(TherapySessionDto therapySessionDto) {

        Button updateBtn = createStyledButton("", "update-btn");
        Button deleteBtn = createStyledButton("", "delete-btn");
        deleteBtn.setGraphic(MaterialIconFactory.get().createIcon(MaterialIcon.DELETE, "16px"));
        updateBtn.setGraphic(MaterialIconFactory.get().createIcon(MaterialIcon.EDIT, "16px"));

        setDeleteButtonAction(deleteBtn,therapySessionDto);
        setUpdateButtonAction(updateBtn,therapySessionDto);


        TherapySessionTm therapySessionTm = new TherapySessionTm();

        therapySessionTm.setId(therapySessionDto.getId());
        therapySessionTm.setProgramme(therapySessionDto.getProgramme());
        therapySessionTm.setTherapist(therapySessionDto.getTherapist());
        therapySessionTm.setDate(therapySessionDto.getDate());
        therapySessionTm.setStartTime(therapySessionDto.getStartTime());
        therapySessionTm.setEndTime(therapySessionDto.getEndTime());
        therapySessionTm.setBtnDelete(deleteBtn);
        therapySessionTm.setBtnUpdate(updateBtn);

        return therapySessionTm;
    }

    private void setUpdateButtonAction(Button updateBtn, TherapySessionDto therapySessionDto) {
        updateBtn.setOnAction(event -> {
            SessionActionFormController actionFormController = NavigationUtil.loadSubStage(
                    SessionTableFormController.class,
                    "/view/sessionAction.fxml",
                    "update  session schedule",
                    updateBtn.getScene().getWindow()
            );

            if (actionFormController != null) {
                actionFormController.setTherapySessionData(therapySessionDto);
                actionFormController.setActionButtonText("Update");
                actionFormController.setLblTitle("SCHEDULE UPDATE");
                actionFormController.setSessionTableFormController(this);
                this.sessionActionFormController = actionFormController;
            }
        });

    }

    private void setDeleteButtonAction(Button deleteBtn, TherapySessionDto therapySessionDto) {

        deleteBtn.setOnAction(event -> {
            if (AlertUtil.setConfirmationAlert("Delete", "Are you sure you want to delete this Schedule?")) {
                try {
                    boolean isDeleted = therapySessionBo.deleteTherapySession(therapySessionDto.getId());
                    if (isDeleted) {
                        loadSessionTable();
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Session Schedule deleted successfully", true);
                    } else {
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Cannot delete this Session because it is already in use or another fault!!!", false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Button createStyledButton(String text, String styleClass) {
        Button button = new Button(text);
        button.setStyle("-fx-font-weight: bold;");
        button.getStyleClass().add(styleClass);
        return button;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProgrammeName.setCellValueFactory(new PropertyValueFactory<>("programme"));
        colTherapistName.setCellValueFactory(new PropertyValueFactory<>("therapist"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("btnUpdate"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("btnDelete"));

        tblSession.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        loadSessionTable();

    }
}
