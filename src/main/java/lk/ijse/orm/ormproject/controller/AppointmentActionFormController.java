package lk.ijse.orm.ormproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.orm.ormproject.bo.BoFactory;
import lk.ijse.orm.ormproject.bo.BoTypes;
import lk.ijse.orm.ormproject.bo.custom.AppointmentBo;
import lk.ijse.orm.ormproject.dto.AppointmentDto;
import lk.ijse.orm.ormproject.dto.PatientDto;
import lk.ijse.orm.ormproject.dto.TherapySessionDto;
import lk.ijse.orm.ormproject.exception.MissingFieldException;
import lk.ijse.orm.ormproject.util.AlertUtil;
import lombok.Getter;
import lombok.Setter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class AppointmentActionFormController implements Initializable {

    AppointmentBo appointmentBo = BoFactory.getInstance().getBo(BoTypes.APPOINTMENT);

    @Getter
    @Setter
    private AppointmentTableFormController appointmentTableFormController;

    @FXML
    private Button btnAction;

    @FXML
    private Button btnClear;


    @FXML
    private ComboBox<String> cmbPatient;

    @FXML
    private ComboBox<String> cmbSession;

    @FXML
    private Label lblID;

    @FXML
    private Label lblEndTime;

    @FXML
    private Label lblStartTime;


    @FXML
    private Label lblScheduledDate;

    @FXML
    private Label lblPatientName;

    @FXML
    private Label lblSessionName;

    @FXML
    private Label lblTherapist;

    @FXML
    private Label lblTitle;

    @FXML
    void btnActionOnMouseClicked(MouseEvent event) throws Exception {

        if (btnAction.getText().equalsIgnoreCase("Save")) {
            if (isSaveOrUpdate()) {
                AppointmentDto appointmentDto = new AppointmentDto();


                appointmentDto.setId(lblID.getText());
                appointmentDto.setPatient(cmbPatient.getValue());
                appointmentDto.setSession(cmbSession.getValue());
                appointmentDto.setDate(LocalDate.parse(lblScheduledDate.getText()));

                LocalTime fullTime = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String formattedTime = fullTime.format(formatter);

                appointmentDto.setTime(LocalTime.parse(formattedTime));

                try {
                    boolean isSaved = appointmentBo.saveAppointment(appointmentDto);
                    if (isSaved) {
                        String email = appointmentBo.getPatientEmailForSendAppointmentDetails(cmbPatient.getValue());
                        String sessionID = cmbSession.getValue();

                        Thread appointment = new Thread(() -> {
                            String from = "serenity.mht.center@gmail.com";
                            String host = "smtp.gmail.com";
                            String username = "serenity.mht.center@gmail.com";
                            String password = "huzc fcve qmzq dohd"; // App password

                            Properties props = new Properties();
                            props.put("mail.smtp.auth", "true");
                            props.put("mail.smtp.starttls.enable", "true");
                            props.put("mail.smtp.host", host);
                            props.put("mail.smtp.port", "587");

                            Session session = Session.getInstance(props, new Authenticator() {
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password);
                                }
                            });

                            try {
                                Message message = new MimeMessage(session);
                                message.setFrom(new InternetAddress(from));
                                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                                message.setSubject("Your Appointment Confirmation");


                                String htmlTemplate = String.format("""
<html>
<head>
    <meta charset="UTF-8">
    <title>%s</title>
    <style>
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(to right, #e0f7fa, #e1bee7);
            margin: 0;
            padding: 30px 0;
            color: #333;
        }

        .container {
            max-width: 650px;
            margin: auto;
            background-color: #fff;
            border-radius: 15px;
            box-shadow: 0 5px 25px rgba(0, 0, 0, 0.1);
            animation: fadeIn 0.8s ease-in-out;
            overflow: hidden;
        }

        .header {
            background: linear-gradient(135deg, #42a5f5, #7e57c2);
            color: #fff;
            padding: 25px 30px;
            text-align: center;
        }

        .header h2 {
            margin: 0;
            font-size: 26px;
        }

        .content {
            padding: 30px 35px;
            line-height: 1.7;
            font-size: 16px;
        }

        .content p {
            margin-bottom: 20px;
        }

        .highlight {
            color: #00897b;
            font-weight: bold;
        }

        .footer {
            background-color: #f8f8f8;
            padding: 20px;
            text-align: center;
            font-size: 13px;
            color: #999;
        }

        .signature {
            margin-top: 25px;
            font-style: italic;
            color: #555;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>%s</h2>
        </div>
        <div class="content">
            <p>Dear <strong>%s</strong>,</p>
            <p>We are pleased to inform you that your appointment has been successfully scheduled with the <span class="highlight">Serenity Mental Health Center</span>.</p>
            <p><strong>Appointment ID:</strong> <span class="highlight">%s</span></p>
            <p><strong>Session Name:</strong> <span class="highlight">%s</span></p>
            <p><strong>Therapist:</strong> <span class="highlight">%s</span></p>
            <p><strong>Scheduled Date:</strong> <span class="highlight">%s</span></p>
            <p><strong>Appointments Placed:</strong> <span class="highlight">%s</span></p>
            <p>If you have any questions or need assistance, feel free to contact us anytime.</p>
            <p class="signature">Warm Regards,<br>Serenity Mental Health Center Team</p>
        </div>
        <div class="footer">
            &copy; 2025 Serenity Center - Auto-generated Email
        </div>
    </div>
</body>
</html>
""", "Your Appointment Confirmation", "Serenity Mental Health Center", lblPatientName.getText(), lblID.getText(), lblSessionName.getText(), lblTherapist.getText(), lblScheduledDate.getText(), appointmentBo.getAppointmentPlacedNumberBySessionId(sessionID));

                                message.setContent(htmlTemplate, "text/html");
                                Transport.send(message);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        appointment.start();

                        resetFields();
                        appointmentTableFormController.loadTable();
                        AlertUtil.setInformationAlert(getClass(),"","Appointment booking successfully." , true);
                    }else {
                        AlertUtil.setInformationAlert(getClass(),"","Cannot booking appointment!!!." , false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else {
                AlertUtil.setInformationAlert(
                        getClass(),
                        "",
                        new MissingFieldException("Required field is missing.\nPlease provide all necessary information.").getMessage()
                        ,true
                );
            }
        }else {
            if (isSaveOrUpdate()) {
                AppointmentDto appointmentDto = new AppointmentDto();
                appointmentDto.setId(lblID.getText());
                appointmentDto.setPatient(cmbPatient.getValue());
                appointmentDto.setSession(cmbSession.getValue());
                appointmentDto.setDate(LocalDate.parse(lblScheduledDate.getText()));

                LocalTime fullTime = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String formattedTime = fullTime.format(formatter);

                appointmentDto.setTime(LocalTime.parse(formattedTime));

                try {
                    boolean isUpdate = appointmentBo.updateAppointment(appointmentDto);
                    if (isUpdate) {
                        resetFields();
                        appointmentTableFormController.loadTable();
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Appointment updated successfully!", true);
                        Stage stage = (Stage) btnAction.getScene().getWindow();
                        stage.close();
                    } else {
                        AlertUtil.setInformationAlert(UserActionFormController.class, "", "Can't update Appointment", false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


    }

    @FXML
    void btnClearOnMouseClicked(MouseEvent event) {
        resetFields();

    }

    @FXML
    void cmbPatientOnSelectAction(ActionEvent event) {
        String selectedValue = cmbPatient.getSelectionModel().getSelectedItem();
        try {
            List<PatientDto> allPatientsForAppointment = appointmentBo.getAllPatientsForAppointment();
            for (PatientDto patientDto : allPatientsForAppointment) {
                if (patientDto.getId().equals(selectedValue)) {
                    if (selectedValue != null) {
                        lblPatientName.setText(patientDto.getName());
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    void cmbSessionOnSelectAction(ActionEvent event) {
        String selectedValue = cmbSession.getSelectionModel().getSelectedItem();
        try {
            List<TherapySessionDto> allSessionsForAppointment = appointmentBo.getAllSessionsForAppointment();
            for (TherapySessionDto therapySessionDto : allSessionsForAppointment) {
                if (therapySessionDto.getId().equals(selectedValue)) {
                    if (selectedValue != null) {
                        lblSessionName.setText(therapySessionDto.getProgramme());
                        lblTherapist.setText(therapySessionDto.getTherapist());
                        lblScheduledDate.setText(String.valueOf(therapySessionDto.getDate()));
                        lblEndTime.setText(String.valueOf(therapySessionDto.getEndTime()));
                        lblStartTime.setText(String.valueOf(therapySessionDto.getStartTime()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            lblID.setText(appointmentBo.generateNewAppointmentID());
            List<PatientDto> allPatientsForAppointment = appointmentBo.getAllPatientsForAppointment();

            List<String> patientIds = new ArrayList<>();
            for (PatientDto patientDto : allPatientsForAppointment) {
                patientIds.add(patientDto.getId());
            }

            ObservableList<String> patientObservableList = FXCollections.observableArrayList(patientIds);
            cmbPatient.setItems(patientObservableList);


            List<TherapySessionDto> allSessionsForAppointment = appointmentBo.getAllSessionsForAppointment();

            List<String> sessionIds = new ArrayList<>();
            for (TherapySessionDto therapySessionDto : allSessionsForAppointment) {
                sessionIds.add(therapySessionDto.getId());
            }

            ObservableList<String> sessionObservableList = FXCollections.observableArrayList(sessionIds);
            cmbSession.setItems(sessionObservableList);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void resetFields() {
        try {
            lblID.setText(appointmentBo.generateNewAppointmentID());
            cmbPatient.getSelectionModel().clearSelection();
            cmbSession.getSelectionModel().clearSelection();
            lblPatientName.setText("");
            lblSessionName.setText("");
            lblTherapist.setText("");
            lblScheduledDate.setText("");

            lblEndTime.setText("");
            lblStartTime.setText("");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isSaveOrUpdate() {
        return cmbPatient.getSelectionModel().getSelectedItem() != null &&
                cmbSession.getSelectionModel().getSelectedItem() != null;
    }

    public void setPatientActionBtnText(String update) {
        btnAction.setText(update);
    }

    public void setLblTitle(String patientUpdate) {
        lblTitle.setText(patientUpdate);
    }

    public void setAppoinmentData(AppointmentDto appointmentDto) {
        lblID.setText(appointmentDto.getId());
        cmbSession.getSelectionModel().select(appointmentDto.getSession());
        cmbPatient.getSelectionModel().select(appointmentDto.getPatient());
    }
}
