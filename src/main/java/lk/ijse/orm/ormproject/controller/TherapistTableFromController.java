package lk.ijse.orm.ormproject.controller;


import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lk.ijse.orm.ormproject.bo.BoFactory;
import lk.ijse.orm.ormproject.bo.BoTypes;
import lk.ijse.orm.ormproject.bo.custom.TherapistBo;
import lk.ijse.orm.ormproject.dto.TherapistDto;
import lk.ijse.orm.ormproject.dto.tm.TherapistTm;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lk.ijse.orm.ormproject.util.NavigationUtil;
import lombok.Getter;
import lombok.Setter;
import org.controlsfx.glyphfont.FontAwesome;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class TherapistTableFromController implements Initializable {

    TherapistBo therapistBo = BoFactory.getInstance().getBo(BoTypes.THERAPIST);

    @Getter
    @Setter
    private TherapistActionFormController therapistActionFormController;

    @FXML
    private Button btnAdd;

    @FXML
    private TableColumn<TherapistTm, String> colID;

    @FXML
    private TableColumn<TherapistTm, Button> colTherapistDelete;

    @FXML
    private TableColumn<TherapistTm, String> colTherapistEmail;

    @FXML
    private TableColumn<TherapistTm, String> colTherapistName;

    @FXML
    private TableColumn<TherapistTm, String> colTherapistProgramme;

    @FXML
    private TableColumn<TherapistTm, Button> colTherapistUpdate;

    @FXML
    private TableColumn<TherapistTm, String> colTherapistContactNumber;

    @FXML
    private TableView<TherapistTm> tblTherapist;

    @FXML
    private Pane therapistPane;

    @FXML
    private TextField txtFindTherapist;


    FilteredList filter;



    @FXML
    void btnAddOnMouseClicked(MouseEvent event) {
        TherapistActionFormController therapistActionFormController = NavigationUtil.loadSubStage(
                UserTableFormController.class,
                "/view/therapistAction.fxml",
                "Therapist registration",
                btnAdd.getScene().getWindow()

        );

        if (therapistActionFormController != null) {
            therapistActionFormController.setTherapistTableFromController(this);
            this.therapistActionFormController = therapistActionFormController;
            therapistActionFormController.setActionButtonText("Save");
        }

    }

    public void loadTherapistTable() {
        try {
            List<TherapistDto> allTherapist = therapistBo.getAllTherapist();
            ObservableList<TherapistTm> therapistTms = FXCollections.observableArrayList();
            for (TherapistDto therapistDto : allTherapist) {
                TherapistTm therapistTm = createTableRow(therapistDto);
                therapistTms.add(therapistTm);
            }
            tblTherapist.setItems(therapistTms);

            filter = new FilteredList(therapistTms, e -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private TherapistTm createTableRow(TherapistDto therapistDto) {

//        FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
//        iconView.setStyle("-fx-fill: white; -fx-font-size: 13px;");
//            deleteBtn.setGraphic(iconView);


        Button updateBtn = createStyledButton("", "update-btn");
        Button deleteBtn = createStyledButton("", "delete-btn");
        deleteBtn.setGraphic(MaterialIconFactory.get().createIcon(MaterialIcon.DELETE, "16px"));
        updateBtn.setGraphic(MaterialIconFactory.get().createIcon(MaterialIcon.EDIT, "16px"));


        setDeleteButtonAction(deleteBtn,therapistDto);
        setUpdateButtonAction(updateBtn,therapistDto);

        TherapistTm therapistTm = new TherapistTm();

        therapistTm.setId(therapistDto.getId());
        therapistTm.setName(therapistDto.getName());
        therapistTm.setProgramme(therapistDto.getProgramme());
        therapistTm.setEmail(therapistDto.getEmail());
        therapistTm.setTelephone(therapistDto.getTelephone());
        therapistTm.setBtnDelete(deleteBtn);
        therapistTm.setBtnUpdate(updateBtn);

        return therapistTm;



    }

    private void setUpdateButtonAction(Button updateBtn, TherapistDto therapistDto) {
        updateBtn.setOnAction(event -> {
            TherapistActionFormController actionFormController = NavigationUtil.loadSubStage(
                    TherapistTableFromController.class,
                    "/view/therapistAction.fxml",
                    "update user",
                    updateBtn.getScene().getWindow()
            );

            if (actionFormController != null) {
                actionFormController.setTherapistData(therapistDto);
                actionFormController.setActionButtonText("Update");
                actionFormController.setLblTitle("THERAPIST UPDATE");
                actionFormController.setTherapistTableFromController(this);
                this.therapistActionFormController = actionFormController;
            }
        });
    }

    private void setDeleteButtonAction(Button deleteBtn, TherapistDto therapistDto) {
        deleteBtn.setOnAction(event -> {
            if (AlertUtil.setConfirmationAlert("Delete", "Are you sure you want to delete this Therapist?")) {
                try {
                    boolean isDeleted = therapistBo.deleteTherapist(therapistDto.getId());
                    if (isDeleted) {
                        loadTherapistTable();
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Therapist deleted successfully", true);
                    } else {
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Cannot delete this Therapist because it is already in use or another fault!!!", false);
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
        colTherapistDelete.setCellValueFactory(new PropertyValueFactory<>("btnDelete"));
        colTherapistEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTherapistName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTherapistUpdate.setCellValueFactory(new PropertyValueFactory<>("btnUpdate"));
        colTherapistProgramme.setCellValueFactory(new PropertyValueFactory<>("programme"));
        colTherapistContactNumber.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        tblTherapist.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        loadTherapistTable();

    }

    public void txtFindTherapistOnKeyAction(KeyEvent keyEvent) {
        txtFindTherapist.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super TherapistTm>) (TherapistTm therapistTm) -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Return all subjects if the search text is empty
                } else {
                    // Perform case-insensitive matching
                    return therapistTm.getId().toLowerCase().contains(newValue.toLowerCase()) ||
                            therapistTm.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                            therapistTm.getProgramme().toLowerCase().contains(newValue.toLowerCase()) ||
                            therapistTm.getTelephone().toLowerCase().contains(newValue.toLowerCase()) ||
                            therapistTm.getEmail().toLowerCase().contains(newValue.toLowerCase());
                }
            });

            SortedList<TherapistTm> sortedList = new SortedList<>(filter);
            sortedList.comparatorProperty().bind(tblTherapist.comparatorProperty());
            tblTherapist.setItems(sortedList);
        });
    }
}
