package io.github.oosquare.neocourse.application.query.schedule;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.infrastructure.repository.schedule.ScheduleMapper;
import io.github.oosquare.neocourse.utility.exception.EntityNotFoundException;
import io.github.oosquare.neocourse.utility.id.Id;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduleQueryService {

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
                .build());
    }
}
