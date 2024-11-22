package io.github.oosquare.neocourse.application.query.schedule;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.infrastructure.repository.schedule.RegistrationEvaluationProjection;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RegistrationEvaluationRepresentation {

    private final @NonNull String studentId;
    private final @NonNull String studentName;
    private final @NonNull String participationStatus;

    public static RegistrationEvaluationRepresentation fromData(
        @NonNull RegistrationEvaluationProjection data
    ) {
        var participationStatus = switch (data.getParticipationStatus()) {
            case ABSENT -> "Absent";
            case ATTENDED -> "Attended";
        };
        return RegistrationEvaluationRepresentation.builder()
            .studentId(data.getStudentId())
            .studentName(data.getStudentName())
            .participationStatus(participationStatus)
            .build();
    }
}
