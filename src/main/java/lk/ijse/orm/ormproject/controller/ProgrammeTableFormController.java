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
import lk.ijse.orm.ormproject.bo.custom.ProgrammeBo;
import lk.ijse.orm.ormproject.dto.ProgrammeDto;
import lk.ijse.orm.ormproject.dto.UserDto;
import lk.ijse.orm.ormproject.dto.tm.ProgrammeTm;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lk.ijse.orm.ormproject.util.NavigationUtil;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class ProgrammeTableFormController implements Initializable {


    private ProgrammeActionFormController programmeActionFormController;

    @FXML
    private Button btnAdd;

    @FXML
    private TableColumn<ProgrammeTm, String> colDuration;

    @FXML
    private TableColumn<ProgrammeTm, BigDecimal> colFee;

    @FXML
    private TableColumn<ProgrammeTm, String> colID;

    @FXML
    private TableColumn<ProgrammeTm, Button> colProgrammeDelete;

    @FXML
    private TableColumn<ProgrammeTm, String> colProgrammeName;

    @FXML
    private TableColumn<ProgrammeTm, Button> colProgrammeUpdate;

    @FXML
    private Pane programmePane;

    @FXML
    private TextField txtFindProgramme;


    @FXML
    private TableView<ProgrammeTm> tblProgramme;

    FilteredList filter;

    ProgrammeBo programmeBo = BoFactory.getInstance().getBo(BoTypes.PROGRAMME);


    @FXML
    void btnAddOnMouseClicked(MouseEvent event) {
        ProgrammeActionFormController programmeActionFormController = NavigationUtil.loadSubStage(
                ProgrammeTableFormController.class,
                "/view/programmeAction.fxml",
                "programme registration",
                btnAdd.getScene().getWindow()

        );

        if (programmeActionFormController != null) {
            programmeActionFormController.setProgrammeTableFormController(this);
            this.programmeActionFormController = programmeActionFormController;
        }


    }

    private ProgrammeTm createTableRow(ProgrammeDto programmeDto) {

        Button updateBtn = createStyledButton("", "update-btn");
        Button deleteBtn = createStyledButton("", "delete-btn");

        deleteBtn.setGraphic(MaterialIconFactory.get().createIcon(MaterialIcon.DELETE, "16px"));
        updateBtn.setGraphic(MaterialIconFactory.get().createIcon(MaterialIcon.EDIT, "16px"));


        setDeleteButtonAction(deleteBtn,programmeDto);
        setUpdateButtonAction(updateBtn,programmeDto);

        ProgrammeTm programmeTm = new ProgrammeTm();
        programmeTm.setId(programmeDto.getId());
        programmeTm.setProgrammeName(programmeDto.getProgrammeName());
        programmeTm.setDuration(programmeDto.getDuration());
        programmeTm.setFee(programmeDto.getFee());
        programmeTm.setUpdateBtn(updateBtn);
        programmeTm.setDeleteBtn(deleteBtn);
        return programmeTm;
    }

    private Button createStyledButton(String text, String styleClass) {
        Button button = new Button(text);
        button.setStyle("-fx-font-weight: bold;");
        button.getStyleClass().add(styleClass);
        return button;
    }

    public void loadProgrammeTable() {
        try {
            List<ProgrammeDto> allProgrammes = programmeBo.getAllProgrammes();
            ObservableList<ProgrammeTm> programmeTms = FXCollections.observableArrayList();
            for (ProgrammeDto programmeDto : allProgrammes) {
                ProgrammeTm programmeTm = createTableRow(programmeDto);
                programmeTms.add(programmeTm);

            }
            tblProgramme.setItems(programmeTms);

            filter = new FilteredList(programmeTms, e -> true);



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProgrammeName.setCellValueFactory(new PropertyValueFactory<>("programmeName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        colProgrammeUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colProgrammeDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));


        tblProgramme.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadProgrammeTable();



    }

    private void setDeleteButtonAction(Button deleteBtn, ProgrammeDto programmeDto) {
        deleteBtn.setOnAction(event -> {
            if (AlertUtil.setConfirmationAlert("Delete", "Are you sure you want to delete this Programme?")) {
                try {
                    boolean isDeleted = programmeBo.deleteProgramme(programmeDto.getId());
                    if (isDeleted) {
                       loadProgrammeTable();
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Programme deleted successfully", true);
                    } else {
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Cannot delete this programme because it is already in use or another fault!!!", false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setUpdateButtonAction(Button updateBtn, ProgrammeDto programmeDto) {
        updateBtn.setOnAction(event -> {
            ProgrammeActionFormController actionFormController = NavigationUtil.loadSubStage(
                    UserTableFormController.class,
                    "/view/programmeAction.fxml",
                    "update user",
                    updateBtn.getScene().getWindow()
            );


            if (actionFormController != null) {
                actionFormController.setProgrammeData(programmeDto);
                actionFormController.setActionButtonText("Update");
                actionFormController.setLblTitle("PROGRAMME UPDATE");
                actionFormController.setProgrammeTableFormController(this);
                this.programmeActionFormController = actionFormController;
            }
        });


    }


    public void txtFindProgrammeOnKeyAction(KeyEvent keyEvent) {

        txtFindProgramme.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super ProgrammeTm>) (ProgrammeTm programmeTm) -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Return all subjects if the search text is empty
                } else {
                    // Perform case-insensitive matching
                    return programmeTm.getId().toLowerCase().contains(newValue.toLowerCase()) ||
                            programmeTm.getProgrammeName().toLowerCase().contains(newValue.toLowerCase()) ||
                            programmeTm.getDuration().toLowerCase().contains(newValue.toLowerCase()) ||
                            String.valueOf(programmeTm.getFee()).toLowerCase().contains(newValue.toLowerCase());
                }
            });

            SortedList<ProgrammeTm> sortedList = new SortedList<>(filter);
            sortedList.comparatorProperty().bind(tblProgramme.comparatorProperty());
            tblProgramme.setItems(sortedList);
        });
    }
}
