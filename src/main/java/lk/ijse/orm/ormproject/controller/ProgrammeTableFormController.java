package lk.ijse.orm.ormproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ProgrammeTableFormController {

    @FXML
    private Button btnAdd;

    @FXML
    private TableColumn<?, ?> colDuration;

    @FXML
    private TableColumn<?, ?> colFee;

    @FXML
    private TableColumn<?, ?> colID;

    @FXML
    private TableColumn<?, ?> colProgrammeDelete;

    @FXML
    private TableColumn<?, ?> colProgrammeName;

    @FXML
    private TableColumn<?, ?> colProgrammeUpdate;

    @FXML
    private Pane programmePane;

    @FXML
    private TableView<?> tblProgramme;


    @FXML
    void btnAddOnMouseClicked(MouseEvent event) {


    }

}
