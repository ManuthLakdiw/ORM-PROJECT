package lk.ijse.orm.ormproject.controller;

import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import lk.ijse.orm.ormproject.bo.BoFactory;
import lk.ijse.orm.ormproject.bo.BoTypes;
import lk.ijse.orm.ormproject.bo.custom.PatientBo;
import lk.ijse.orm.ormproject.dto.PatientDto;
import lk.ijse.orm.ormproject.dto.tm.PatientTm;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lk.ijse.orm.ormproject.util.NavigationUtil;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class PatientTableFormController implements Initializable {

    @Getter
    @Setter
    private PatientActionFormController patientActionFormController;


    @Getter
    @Setter
    private PatientProfileController patientProfileController;

    @FXML
    private Button btnAdd;

    @FXML
    private TableColumn<PatientTm, Button> colDelete;

    @FXML
    private TableColumn<PatientTm, String> colEmail;

    @FXML
    private TableColumn<PatientTm, String> colID;

    @FXML
    private TableColumn<PatientTm, String> colPatientName;

    @FXML
    private TableColumn<PatientTm, Button> colProfile;

    @FXML
    private TableColumn<PatientTm, Button> colUpdate;

    @FXML
    private Button btnAppoinmentHistory;

    @FXML
    private Pane patientPane;

    @FXML
    private TableView<PatientTm> tblPatient;


    @FXML
    private TextField txtFindPatient;


    FilteredList filter;

    PatientBo patientBo = BoFactory.getInstance().getBo(BoTypes.PATIENT);

    @FXML
    void btnAddOnMouseClicked(MouseEvent event) {
        PatientActionFormController patientActionFormController = NavigationUtil.loadSubStage(
                UserTableFormController.class,
                "/view/patientAction.fxml",
                "user registration",
                btnAdd.getScene().getWindow()

        );

        if (patientActionFormController != null) {
            patientActionFormController.setPatientTableFormController(this);
            patientActionFormController.setPatientActionBtnText("Save");
            this.patientActionFormController = patientActionFormController;
        }


    }

    public void loadPatientTable() {
        try {
            List<PatientDto> allPatients = patientBo.getAllPatients();

            ObservableList<PatientTm> patientTms = FXCollections.observableArrayList();
            for (PatientDto patientDto : allPatients) {
                System.out.println("email load table is : "+ patientDto.getEmailAddress());

                PatientTm patientTm = createTableRow(patientDto);
                patientTms.add(patientTm);
            }

            tblPatient.setItems(patientTms);

            filter = new FilteredList(patientTms, e -> true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PatientTm createTableRow(PatientDto patientDto) {
        Button updateBtn = createStyledButton("", "update-btn");
        Button deleteBtn = createStyledButton("", "delete-btn");
        Button profileBtn = createStyledButton("", "profile-btn");

        deleteBtn.setGraphic(MaterialIconFactory.get().createIcon(MaterialIcon.DELETE, "16px"));
        updateBtn.setGraphic(MaterialIconFactory.get().createIcon(MaterialIcon.EDIT, "16px"));
        profileBtn.setGraphic(MaterialIconFactory.get().createIcon(MaterialIcon.ACCOUNT_CIRCLE, "16px"));

        setDeleteBtnOnAction(deleteBtn,patientDto);
        setUpdateBtnOnAction(updateBtn,patientDto);
        setProfileBtnOnAction(profileBtn,patientDto);

        PatientTm patientTm = new PatientTm();
        patientTm.setId(patientDto.getId());
        patientTm.setTitle(patientDto.getTitle());
        patientTm.setName(patientDto.getName());
        patientTm.setDob(patientDto.getDob());
        patientTm.setHomeAddress(patientDto.getHomeAddress());
        patientTm.setTelephone(patientDto.getTelephone());
        patientTm.setEmailAddress(patientDto.getEmailAddress());
        patientTm.setUpdateBtn(updateBtn);
        patientTm.setDeleteBtn(deleteBtn);
        patientTm.setProfileBtn(profileBtn);


        return patientTm;


    }

    private void setProfileBtnOnAction(Button profileBtn, PatientDto patientDto) {
        profileBtn.setOnAction(event -> {
            PatientProfileController profileController = NavigationUtil.loadSubStage(
                    PatientTableFormController.class,
                    "/view/patientProfile.fxml",
                    "update Patient",
                    profileBtn.getScene().getWindow()
            );

            if (profileController != null) {
                profileController.setPatientTableFormController(this);
                this.patientProfileController = profileController;
                profileController.setPatientDetails(patientDto);

            }
        });
    }

    private void setUpdateBtnOnAction(Button updateBtn, PatientDto patientDto) {
        updateBtn.setOnAction(event -> {
            PatientActionFormController actionFormController = NavigationUtil.loadSubStage(
                    PatientTableFormController.class,
                    "/view/patientAction.fxml",
                    "update Patient",
                    updateBtn.getScene().getWindow()
            );

            if (actionFormController != null) {
                actionFormController.setPatientTableFormController(this);
                this.patientActionFormController = actionFormController;
                patientActionFormController.setPatientActionBtnText("Update");
                patientActionFormController.setLblTitle("PATIENT UPDATE");
                patientActionFormController.setPatientData(patientDto);

            }
        });
    }

    private void setDeleteBtnOnAction(Button deleteBtn, PatientDto patientDto) {
        deleteBtn.setOnAction(event -> {
            if (AlertUtil.setConfirmationAlert("Delete", "Are you sure you want to delete this Patient?")) {
                try {
                    boolean isDeleted = patientBo.deletePatient(patientDto.getId());
                    if (isDeleted) {
                        loadPatientTable();
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Patient deleted successfully", true);
                    }else {
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Failed to delete Patient", false);
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
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colProfile.setCellValueFactory(new PropertyValueFactory<>("profileBtn"));

        tblPatient.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadPatientTable();

    }


    public void txtFindPatientOnKeyAction(KeyEvent keyEvent) {
        txtFindPatient.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super PatientTm>) (PatientTm patientTm) -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Return all subjects if the search text is empty
                } else {
                    // Perform case-insensitive matching
                    return patientTm.getId().toLowerCase().contains(newValue.toLowerCase()) ||
                            patientTm.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                            patientTm.getTitle().toLowerCase().contains(newValue.toLowerCase()) ||
                            patientTm.getEmailAddress().toLowerCase().contains(newValue.toLowerCase()) ||
                            patientTm.getTelephone().toLowerCase().contains(newValue.toLowerCase()) ||
                            patientTm.getHomeAddress().toLowerCase().contains(newValue.toLowerCase());
                }
            });

            SortedList<PatientTm> sortedList = new SortedList<>(filter);
            sortedList.comparatorProperty().bind(tblPatient.comparatorProperty());
            tblPatient.setItems(sortedList);
        });

    }
}
