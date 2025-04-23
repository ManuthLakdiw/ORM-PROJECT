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
import lk.ijse.orm.ormproject.bo.custom.PaymentBo;
import lk.ijse.orm.ormproject.dto.PaymentDto;
import lk.ijse.orm.ormproject.dto.tm.PaymentTm;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lk.ijse.orm.ormproject.util.NavigationUtil;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentTableFormController implements Initializable {

    PaymentBo paymentBo = BoFactory.getInstance().getBo(BoTypes.PAYMENT);

    @Getter
    @Setter
    private PaymentActionFormController paymentActionFormController;

    @FXML
    private Button btnAdd;

    @FXML
    private TableColumn<?, ?> colAppointment;

    @FXML
    private TableColumn<?, ?> colBalanceAmount;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDelete;

    @FXML
    private TableColumn<?, ?> colID;

    @FXML
    private TableColumn<?, ?> colPaidAmount;

    @FXML
    private TableColumn<?, ?> colSessionFee;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colUpdate;

    @FXML
    private Pane paymentPane;

    @FXML
    private TableView<PaymentTm> tblPayment;

    @FXML
    void btnAddOnMouseClicked(MouseEvent event) {
        PaymentActionFormController paymentActionFormController = NavigationUtil.loadSubStage(
                PaymentTableFormController.class,
                "/view/paymentAction.fxml",
                "Payment Manage",
                btnAdd.getScene().getWindow()

        );
        if (paymentActionFormController != null) {
            paymentActionFormController.setPaymentTableFormController(this);
            this.paymentActionFormController = paymentActionFormController;
        }


    }
    public void loadPaymentTable() {
        try {
            List<PaymentDto> allPayments = paymentBo.getAllPayments();
            ObservableList<PaymentTm> paymentTms = FXCollections.observableArrayList();
            for (PaymentDto paymentDto : allPayments) {
                PaymentTm paymentTm = createTableRow(paymentDto);
                paymentTms.add(paymentTm);
            }
            tblPayment.setItems(paymentTms);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private PaymentTm createTableRow(PaymentDto paymentDto) {
        PaymentTm paymentTm = new PaymentTm();

        Button updateBtn = createStyledButton("", "update-btn");
        Button deleteBtn = createStyledButton("", "delete-btn");

        deleteBtn.setGraphic(MaterialIconFactory.get().createIcon(MaterialIcon.DELETE, "16px"));
        updateBtn.setGraphic(MaterialIconFactory.get().createIcon(MaterialIcon.EDIT, "16px"));

        setDeleteButtonAction(deleteBtn,paymentDto);
        setUpdateButtonAction(updateBtn,paymentDto);

        paymentTm.setId(paymentDto.getId());
        paymentTm.setAppointment(paymentDto.getAppointment());
        paymentTm.setSessionFee(paymentDto.getSessionFee());
        paymentTm.setPaidAmount(paymentDto.getPaidAmount());
        paymentTm.setDueAmount(paymentDto.getDueAmount());
        paymentTm.setStatus(paymentDto.getStatus());
        paymentTm.setDate(paymentDto.getDate());
        paymentTm.setUpdateBtn(updateBtn);
        paymentTm.setDeleteBtn(deleteBtn);

        return paymentTm;
    }

    private void setUpdateButtonAction(Button updateBtn, PaymentDto paymentDto) {
        updateBtn.setOnAction(event -> {
            PaymentActionFormController actionFormController = NavigationUtil.loadSubStage(
                    SessionTableFormController.class,
                    "/view/paymentAction.fxml",
                    "update payment schedule",
                    updateBtn.getScene().getWindow()
            );

            if (actionFormController != null) {
                actionFormController.setPaymentData(paymentDto);
                actionFormController.setActionButtonText("Update");
                actionFormController.setLblTitle("SCHEDULE UPDATE");
                actionFormController.setPaymentTableFormController(this);
                this.paymentActionFormController = actionFormController;
            }
        });
    }

    private void setDeleteButtonAction(Button deleteBtn, PaymentDto paymentDto) {
        deleteBtn.setOnAction(event -> {
            if (AlertUtil.setConfirmationAlert("Delete", "Are you sure you want to delete this Payment?")) {
                try {
                    boolean isDeleted = paymentBo.deletePayment(paymentDto.getId());
                    if (isDeleted) {
                        loadPaymentTable();
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Payment deleted successfully", true);
                    } else {
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Cannot delete this Payment because it is already in use or another fault!!!", false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Button createStyledButton(String text, String styleClass) {
        Button button = new Button();
        button.setStyle("-fx-font-weight: bold;");
        button.getStyleClass().add(styleClass);
        return button;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAppointment.setCellValueFactory(new PropertyValueFactory<>("appointment"));
        colSessionFee.setCellValueFactory(new PropertyValueFactory<>("sessionFee"));
        colPaidAmount.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        colBalanceAmount.setCellValueFactory(new PropertyValueFactory<>("dueAmount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));

        tblPayment.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadPaymentTable();
    }
}
