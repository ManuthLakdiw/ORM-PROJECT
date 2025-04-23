package lk.ijse.orm.ormproject.bo.custom.impl;

import lk.ijse.orm.ormproject.bo.custom.PaymentBo;
import lk.ijse.orm.ormproject.dao.DaoFactory;
import lk.ijse.orm.ormproject.dao.DaoTypes;
import lk.ijse.orm.ormproject.dao.custom.AppointmentDao;
import lk.ijse.orm.ormproject.dao.custom.PaymentDao;
import lk.ijse.orm.ormproject.dao.custom.ProgrammeDao;
import lk.ijse.orm.ormproject.dto.AppointmentDto;
import lk.ijse.orm.ormproject.dto.PaymentDto;
import lk.ijse.orm.ormproject.dto.ProgrammeDto;
import lk.ijse.orm.ormproject.entity.Appointment;
import lk.ijse.orm.ormproject.entity.Payment;
import lk.ijse.orm.ormproject.entity.Programme;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public class PaymentBoImpl implements PaymentBo {
    PaymentDao paymentDao = DaoFactory.getInstance().getDao(DaoTypes.PAYMENT);
    AppointmentDao appointmentDao = DaoFactory.getInstance().getDao(DaoTypes.APPOINTMENT);
    ProgrammeDao programmeDao = DaoFactory.getInstance().getDao(DaoTypes.PROGRAMME);

    @Override
    public String generateNewPaymentID() throws Exception {
        Optional<String> lastPk = paymentDao.lastPK();
        if (lastPk.isPresent()) {
            String subString = lastPk.get().substring(3);
            int id = Integer.parseInt(subString);
            int newID = id + 1;
            return String.format("PAY%03d", newID);
        }
        return "PAY001";
    }

    @Override
    public List<PaymentDto> getAllPayments() throws Exception {
        List<PaymentDto> paymentDtos = new ArrayList<>();
        List<Payment> all = paymentDao.getAll();
        for (Payment payment : all) {
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setId(payment.getId());
            paymentDto.setAppointment(payment.getAppointment().getId());
            paymentDto.setSessionFee(payment.getSessionFee());
            paymentDto.setPaidAmount(payment.getPaidAmount());
            paymentDto.setDueAmount(payment.getDueAmount());
            paymentDto.setStatus(payment.getStatus());
            paymentDto.setDate(payment.getDate());
            paymentDtos.add(paymentDto);
        }
        return paymentDtos;
    }

    @Override
    public boolean savePayment(PaymentDto paymentDto) throws Exception {
        Payment payment = new Payment();

        payment.setId(paymentDto.getId());
        Optional<Appointment> byId = appointmentDao.findById(paymentDto.getAppointment());
        if (byId.isPresent()) {
            payment.setAppointment(byId.get());
        }

        payment.setSessionFee(paymentDto.getSessionFee());
        payment.setPaidAmount(paymentDto.getPaidAmount());
        payment.setDueAmount(paymentDto.getDueAmount());

        if (paymentDto.getDueAmount().compareTo(BigDecimal.ZERO) == 0) {
            payment.setStatus("Paid");
        }else {
            payment.setStatus("Not Completed");
        }
        payment.setDate(paymentDto.getDate());
        payment.setPatient(paymentDto.getPatient());

        return paymentDao.save(payment);


    }

    @Override
    public boolean deletePayment(String id) throws Exception {
        return paymentDao.delete(id);
    }

    @Override
    public boolean updatePayment(PaymentDto paymentDto) throws Exception {
        Payment payment = new Payment();

        payment.setId(paymentDto.getId());
        Optional<Appointment> byId = appointmentDao.findById(paymentDto.getAppointment());
        if (byId.isPresent()) {
            payment.setAppointment(byId.get());
        }

        payment.setSessionFee(paymentDto.getSessionFee());
        payment.setPaidAmount(paymentDto.getPaidAmount());
        payment.setDueAmount(paymentDto.getDueAmount());

        if (paymentDto.getDueAmount().compareTo(BigDecimal.ZERO) == 0) {
            payment.setStatus("Paid");
        }else {
            payment.setStatus("Not Completed");
        }
        payment.setDate(paymentDto.getDate());
        payment.setPatient(paymentDto.getPatient());

        return paymentDao.update(payment);
    }

    @Override
    public List<AppointmentDto> getAllAppointmentsForPayments() throws Exception {
        List<Appointment> all = appointmentDao.getAll();
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : all) {
            AppointmentDto appointmentDto = new AppointmentDto();
            appointmentDto.setId(appointment.getId());
            appointmentDto.setPatient(appointment.getPatient().getId());
            appointmentDto.setSession(appointment.getTherapySession().getId());
            appointmentDtos.add(appointmentDto);
        }
        return appointmentDtos;
    }

    @Override
    public List<ProgrammeDto> getAllProgrammesForPayments() throws Exception {
        List<Programme> all = programmeDao.getAll();
        List<ProgrammeDto> programmeDtos = new ArrayList<>();
        for (Programme programme : all) {
            ProgrammeDto programmeDto = new ProgrammeDto();
            programmeDto.setId(programme.getId());
            programmeDto.setFee(programme.getFee());
        }
        return programmeDtos;

    }
}
