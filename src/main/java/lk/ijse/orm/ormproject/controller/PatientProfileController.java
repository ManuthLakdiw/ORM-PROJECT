package lk.ijse.orm.ormproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lk.ijse.orm.ormproject.config.FactoryConfiguration;
import lk.ijse.orm.ormproject.dto.PatientDto;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PatientProfileController {

    @Setter
    @Getter
    private PatientTableFormController patientTableFormController;

    @FXML
    private Label lblAddress;

    @FXML
    private Label lblAge;

    @FXML
    private Label lblDOB;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblID;

    @FXML
    private Label lblName;

    @FXML
    private Label lblProfileTitle;

    @FXML
    private Label lblTelephone;

    @FXML
    private AnchorPane patientProfilePane;


    public void setPatientDetails(PatientDto patient) {
        lblID.setText(patient.getId());
        lblName.setText(patient.getTitle()+". "+patient.getName());
        lblAddress.setText(patient.getHomeAddress());
        lblProfileTitle.setText(patient.getName()+"'s Profile");
        lblTelephone.setText(patient.getTelephone());
        lblDOB.setText(String.valueOf(patient.getDob()));
        lblEmail.setText(patient.getEmailAddress());
        lblAge.setText(String.valueOf(getPatientCurrentAge(patient.getDob())));

    }

    public int getPatientCurrentAge(LocalDate birthDate) {
        return LocalDate.now().getYear() - birthDate.getYear();
    }


    @FXML
    public void btnAppoinmentHistoryOnClick(ActionEvent actionEvent) {
//        try {
//
//            Session hibernateSession = FactoryConfiguration.getInstance().getSession();
//            Connection hibernateConn = hibernateSession.doReturningWork(connection -> connection);
//            InputStream reportStream = getClass().getResourceAsStream("/reports/allPatients.jrxml");
//            JasperDesign design = JRXmlLoader.load(reportStream);
//            JasperReport jasperReport = JasperCompileManager.compileReport(design);
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null,hibernateConn);
//            JasperViewer.viewReport(jasperPrint, false);
//
//
//        } catch (JRException e) {
//            e.printStackTrace();
//        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("patient_id", lblID.getText());
            parameters.put("patientName", lblName.getText());
            parameters.put("contact_number", lblTelephone.getText());
            Session hibernateSession = FactoryConfiguration.getInstance().getSession();
            Connection hibernateConn = hibernateSession.doReturningWork(connection -> connection);
            InputStream reportStream = getClass().getResourceAsStream("/reports/appointmentsByPatient.jrxml");
            JasperDesign design = JRXmlLoader.load(reportStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,hibernateConn);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
