package io.github.oosquare.neocourse.infrastructure.repository.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationEvaluationProjection {

    private String studentId;
    private String studentName;
    private ParticipationStatusData participationStatus;
}
