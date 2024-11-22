package io.github.oosquare.neocourse.application.query.schedule;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.infrastructure.repository.schedule.ScheduleEvaluationProjection;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class ScheduleEvaluationRepresentation {

    private final @NonNull String id;
    private final @NonNull String courseName;
    private final @NonNull List<RegistrationEvaluationRepresentation> registrations;

    public static ScheduleEvaluationRepresentation fromData(
        @NonNull ScheduleEvaluationProjection data
    ) {
        return ScheduleEvaluationRepresentation.builder()
            .id(data.getId())
            .courseName(data.getCourseName())
            .registrations(data.getRegistrations()
                .stream()
                .map(RegistrationEvaluationRepresentation::fromData)
                .toList())
            .build();
    }
}
