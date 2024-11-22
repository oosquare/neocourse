package io.github.oosquare.neocourse.infrastructure.repository.schedule;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScheduleEvaluationProjection {

    private String id;
    private String courseName;
    private List<RegistrationEvaluationProjection> registrations;
}
