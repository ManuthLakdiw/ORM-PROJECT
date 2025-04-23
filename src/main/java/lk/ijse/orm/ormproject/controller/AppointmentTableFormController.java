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
import lk.ijse.orm.ormproject.bo.custom.AppointmentBo;
import lk.ijse.orm.ormproject.dto.AppointmentDto;
import lk.ijse.orm.ormproject.dto.tm.AppointmentTm;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lk.ijse.orm.ormproject.util.NavigationUtil;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class AppointmentTableFormController implements Initializable {

    AppointmentBo appointmentBo = BoFactory.getInstance().getBo(BoTypes.APPOINTMENT);

    @Getter
    @Setter
    private AppointmentActionFormController appointmentActionFormController;

    @FXML
    private Pane appointmentPane;

    @FXML
    private Button btnAdd;

    @FXML
    private TableColumn<?, ?> colDelete;

    @FXML
    private TableColumn<?, ?> colID;

    @FXML
    private TableColumn<?, ?> colPatient;

    @FXML
    private TableColumn<?, ?> colSession;

    @FXML
    private TableColumn<?, ?> colTIme;

    @FXML
    private TableColumn<?, ?> colUpdate;

    @FXML
    private TableView<AppointmentTm> tblAppointment;

    @FXML
    void btnAddOnMouseClicked(MouseEvent event) {
        AppointmentActionFormController appointmentActionFormController = NavigationUtil.loadSubStage(
                AppointmentTableFormController.class,
                "/view/appointmentAction.fxml",
                "Appointment",
                btnAdd.getScene().getWindow()

        );

        if (appointmentActionFormController != null) {
            appointmentActionFormController.setAppointmentTableFormController(this);
            this.appointmentActionFormController = appointmentActionFormController;
        }

    }

    public void loadTable() {
        try {
            List<AppointmentDto> allAppointment = appointmentBo.getAllAppointment();
            ObservableList<AppointmentTm> appointmentTms = FXCollections.observableArrayList();
            for (AppointmentDto appointmentDto : allAppointment) {
                AppointmentTm appointmentTm = createTableRow(appointmentDto);
                appointmentTms.add(appointmentTm);
            }
            tblAppointment.setItems(appointmentTms);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private AppointmentTm createTableRow(AppointmentDto appointmentDto) {
        AppointmentTm appointmentTm = new AppointmentTm();

        Button updateBtn = createStyledButton("", "update-btn");
        Button deleteBtn = createStyledButton("", "delete-btn");

        deleteBtn.setGraphic(MaterialIconFactory.get().createIcon(MaterialIcon.DELETE, "16px"));
        updateBtn.setGraphic(MaterialIconFactory.get().createIcon(MaterialIcon.EDIT, "16px"));

        setDeleteBtnOnAction(deleteBtn,appointmentDto);
        setUpdateBtnOnAction(updateBtn,appointmentDto);

        appointmentTm.setId(appointmentDto.getId());
        appointmentTm.setPatient(appointmentDto.getPatient());
        appointmentTm.setSession(appointmentDto.getSession());

        LocalTime fullTime = appointmentDto.getTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = fullTime.format(formatter);

        appointmentTm.setTime(LocalTime.parse(formattedTime));
        appointmentTm.setUpdateBtn(updateBtn);
        appointmentTm.setDeleteBtn(deleteBtn);

        return appointmentTm;
    }

    private void setUpdateBtnOnAction(Button updateBtn, AppointmentDto appointmentDto) {
        updateBtn.setOnAction(event -> {
            AppointmentActionFormController  actionFormController = NavigationUtil.loadSubStage(
                    AppointmentTableFormController.class,
                    "/view/appointmentAction.fxml",
                    "update Appointment",
                    updateBtn.getScene().getWindow()
            );

            if (actionFormController != null) {
                actionFormController.setAppointmentTableFormController(this);
                this.appointmentActionFormController = actionFormController;
                appointmentActionFormController.setPatientActionBtnText("Update");
                appointmentActionFormController.setLblTitle("APPOINTMENT UPDATE");
                appointmentActionFormController.setAppoinmentData(appointmentDto);

            }
        });
    }

    private void setDeleteBtnOnAction(Button deleteBtn, AppointmentDto appointmentDto) {
        deleteBtn.setOnAction(event -> {
            if (AlertUtil.setConfirmationAlert("Delete", "Are you sure you want to delete this Appointment?")) {
                try {
                    boolean isDeleted = appointmentBo.deleteAppointment(appointmentDto.getId());
                    if (isDeleted) {
                        loadTable();
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Appointment deleted successfully", true);
                    }else {
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Failed to delete Appointment", false);
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
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patient"));
        colSession.setCellValueFactory(new PropertyValueFactory<>("session"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colTIme.setCellValueFactory(new PropertyValueFactory<>("time"));

        tblAppointment.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        loadTable();
    }
}
