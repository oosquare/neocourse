package io.github.oosquare.neocourse.application.command.registration;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.service.AccountService;
import io.github.oosquare.neocourse.domain.schedule.service.RegistrationService;
import io.github.oosquare.neocourse.domain.schedule.service.ScheduleRepository;
import io.github.oosquare.neocourse.domain.transcript.service.TranscriptRepository;

@Service
@AllArgsConstructor
@Slf4j
public class RegistrationCommandService {

    private final @NonNull AccountService accountService;
    private final @NonNull RegistrationService registrationService;
    private final @NonNull ScheduleRepository scheduleRepository;
    private final @NonNull TranscriptRepository transcriptRepository;

    @Transactional
    public void addRegistration(@NonNull AddRegistrationCommand command, @NonNull Account account) {
        log.info("{} requests addRegistration with {}", account.toLoggingString(), command);

        var scheduleId = command.getScheduleId();

        var student = this.accountService.getStudentUser(account);
        var schedule = this.scheduleRepository.findOrThrow(scheduleId);
        var transcript = this.transcriptRepository.findOrThrow(student.getTranscript());
        var currentTime = ZonedDateTime.now();

        this.registrationService.register(student, schedule, transcript, currentTime);

        this.scheduleRepository.save(schedule);
        this.transcriptRepository.save(transcript);

        log.info("Registered for Schedule[id={}] by {}", schedule.getId(), account.toLoggingString());
    }

    @Transactional
    public void cancelRegistration(@NonNull CancelRegistrationCommand command, @NonNull Account account) {
        log.info("{} requests cancelRegistration with {}", account.toLoggingString(), command);

        var scheduleId = command.getScheduleId();

        var student = this.accountService.getStudentUser(account);
        var schedule = this.scheduleRepository.findOrThrow(scheduleId);
        var transcript = this.transcriptRepository.findOrThrow(student.getTranscript());
        var currentTime = ZonedDateTime.now();

        this.registrationService.cancel(student, schedule, transcript, currentTime);

        this.scheduleRepository.save(schedule);
        this.transcriptRepository.save(transcript);

        log.info(
            "Cancelled registrations for Schedule[id={}] by {}",
            schedule.getId(),
            account.toLoggingString()
        );
    }
}
