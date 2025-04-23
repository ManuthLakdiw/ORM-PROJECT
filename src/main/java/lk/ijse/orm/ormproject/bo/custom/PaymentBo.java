package lk.ijse.orm.ormproject.bo.custom;

import lk.ijse.orm.ormproject.bo.SuperBo;
import lk.ijse.orm.ormproject.dto.AppointmentDto;
import lk.ijse.orm.ormproject.dto.PatientDto;
import lk.ijse.orm.ormproject.dto.PaymentDto;
import lk.ijse.orm.ormproject.dto.ProgrammeDto;
import lk.ijse.orm.ormproject.entity.Programme;

import java.util.List;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public interface PaymentBo  extends SuperBo {
    String generateNewPaymentID() throws Exception;
    List<PaymentDto> getAllPayments() throws Exception;
    boolean savePayment(PaymentDto paymentDto) throws Exception;
    boolean deletePayment(String id) throws Exception;
    boolean updatePayment(PaymentDto paymentDto) throws Exception;
    List<AppointmentDto> getAllAppointmentsForPayments() throws Exception;
    List<ProgrammeDto> getAllProgrammesForPayments() throws Exception;
}
