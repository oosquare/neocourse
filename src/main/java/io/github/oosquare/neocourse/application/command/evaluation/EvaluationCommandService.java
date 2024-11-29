package io.github.oosquare.neocourse.application.command.evaluation;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.common.service.UserService;
import io.github.oosquare.neocourse.domain.schedule.service.ScheduleRepository;
import io.github.oosquare.neocourse.domain.student.service.StudentRepository;
import io.github.oosquare.neocourse.domain.teacher.service.EvaluationService;
import io.github.oosquare.neocourse.domain.transcript.service.TranscriptRepository;

@Service
@AllArgsConstructor
@Slf4j
public class EvaluationCommandService {

    private final @NonNull UserService userService;
    private final @NonNull EvaluationService evaluationService;
    private final @NonNull ScheduleRepository scheduleRepository;
    private final @NonNull StudentRepository studentRepository;
    private final @NonNull TranscriptRepository transcriptRepository;

    @Transactional
    public void gradeStudent(@NonNull GradeStudentCommand command, @NonNull Account account) {
        log.info("{} requests gradeStudent with {}", account.toLoggingString(), command);

        var scheduleId = command.getScheduleId();
        var studentId = command.getStudentId();
        var score = command.getScore();

        var teacher = this.userService.getTeacherUser(account);
        var schedule = this.scheduleRepository.findOrThrow(scheduleId);
        var student = this.studentRepository.findOrThrow(studentId);
        var transcript = this.transcriptRepository.findOrThrow(student.getTranscript());
        var currentTime = ZonedDateTime.now();

        this.evaluationService.gradeStudent(teacher, schedule, student, transcript, score, currentTime);

        this.scheduleRepository.save(schedule);
        this.transcriptRepository.save(transcript);

        log.info(
            "Graded student[id={}, username={}, transcript={}] and gave {} by {}",
            student.getId(),
            student.getUsername(),
            student.getTranscript(),
            score,
            account.toLoggingString()
        );
    }

    @Transactional
    public void markStudentAbsent(@NonNull MarkStudentAbsentCommand command, @NonNull Account account) {
        log.info("{} requests markStudentAbsent with {}", account.toLoggingString(), command);

        var scheduleId = command.getScheduleId();
        var studentId = command.getStudentId();

        var teacher = this.userService.getTeacherUser(account);
        var schedule = this.scheduleRepository.findOrThrow(scheduleId);
        var student = this.studentRepository.findOrThrow(studentId);
        var transcript = this.transcriptRepository.findOrThrow(student.getTranscript());

        this.evaluationService.markStudentAbsent(teacher, schedule, student, transcript);

        this.scheduleRepository.save(schedule);
        this.transcriptRepository.save(transcript);

        log.info(
            "Graded student[id={}, username={}, transcript={}] and marked absent by {}",
            student.getId(),
            student.getUsername(),
            student.getTranscript(),
            account.toLoggingString()
        );
    }
}
