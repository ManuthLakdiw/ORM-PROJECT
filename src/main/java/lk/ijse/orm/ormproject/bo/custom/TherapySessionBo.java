package lk.ijse.orm.ormproject.bo.custom;

import lk.ijse.orm.ormproject.bo.SuperBo;
import lk.ijse.orm.ormproject.dto.TherapySessionDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public interface TherapySessionBo extends SuperBo {
    String generateNewTherapySessionID() throws Exception;
    List<TherapySessionDto> getAllTherapySessions() throws Exception;
    boolean saveTherapySession(TherapySessionDto therapySessionDto) throws Exception;
    boolean deleteTherapySession(String id) throws Exception;
    boolean updateTherapySession(TherapySessionDto therapySessionDto) throws Exception;
    Optional<List<String>> getAllProgrammeNamesForScheduleSession() throws Exception;
    List<String> getAllTherapistNamesByTherapyProgram(String programmeName) throws Exception;
    boolean iScheduleConflict(String id , LocalDate scheduleDate, String program, LocalTime newStartTime, LocalTime newEndTime, String value) throws Exception;
    TherapySessionDto getExitingSession(String id) throws Exception;
}
