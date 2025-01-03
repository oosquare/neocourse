package io.github.oosquare.neocourse.application.query.schedule;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.account.service.AccountService;
import io.github.oosquare.neocourse.infrastructure.repository.schedule.ScheduleMapper;
import io.github.oosquare.neocourse.utility.exception.EntityNotFoundException;
import io.github.oosquare.neocourse.utility.id.Id;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduleQueryService {

    private final @NonNull AccountRepository accountRepository;
    private final @NonNull AccountService accountService;
    private final @NonNull ScheduleMapper scheduleMapper;

    public ScheduleSummaryRepresentation getScheduleByIdInSummaryRepresentation(
        @NonNull Id scheduleId,
        @NonNull Account account
    ) {
        log.info(
            "{} requests getScheduleByIdInSummaryRepresentation with {}",
            account.toLoggingString(),
            scheduleId
        );

        return this.scheduleMapper.findByIdReturningSummaryProjection(scheduleId.getValue())
            .map(ScheduleSummaryRepresentation::fromData)
            .orElseThrow(() -> EntityNotFoundException.builder()
                .entity(ScheduleEvaluationRepresentation.class)
                .context("scheduleId", scheduleId)
                .build());
    }

    @Transactional
    public List<ScheduleSummaryRepresentation> getAllSchedulesInSummaryRepresentation(
        @NonNull Account account
    ) {
        log.info("{} requests getAllSchedulesInSummaryRepresentation", account.toLoggingString());

        return this.scheduleMapper.findAllReturningSummaryProjection()
            .stream()
            .map(ScheduleSummaryRepresentation::fromData)
            .toList();
    }

    @Transactional
    public List<ScheduleSummaryRepresentation> getAllSchedulesByAccountInSummaryRepresentation(
        @NonNull ByAccountQuery query,
        @NonNull Account account
    ) {
        log.info(
            "{} requests getAllSchedulesByAccountInSummaryRepresentation with {}",
            account.toLoggingString(),
            query
        );

        var accountId = query.getAccountId();

        var accountToQuery = this.accountRepository.findOrThrow(accountId);
        var student = this.accountService.getStudentUser(accountToQuery);

        return this.scheduleMapper.findAllByStudentReturningSummaryProjection(student.getId().getValue())
            .stream()
            .map(ScheduleSummaryRepresentation::fromData)
            .toList();
    }

    @Transactional
    public ScheduleEvaluationRepresentation getScheduleByIdInEvaluationRepresentation(
        @NonNull Id scheduleId,
        @NonNull Account account
    ) {
        log.info(
            "{} requests getScheduleByIdInEvaluationRepresentation with {}",
            account.toLoggingString(),
            scheduleId
        );

        return this.scheduleMapper.findByIdReturningEvaluationProjection(scheduleId.getValue())
            .map(ScheduleEvaluationRepresentation::fromData)
            .orElseThrow(() -> EntityNotFoundException.builder()
                .entity(ScheduleEvaluationRepresentation.class)
                .context("scheduleId", scheduleId)
                .build());
    }
}
