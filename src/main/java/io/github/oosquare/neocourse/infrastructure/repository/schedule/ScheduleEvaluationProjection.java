package io.github.oosquare.neocourse.infrastructure.repository.schedule;

import java.util.List;

import lombok.Data;

@Data
public class ScheduleEvaluationProjection {

    private String id;
    private String courseId;
    private String courseName;
    private List<RegistrationEvaluationProjection> registrations;

    public ScheduleEvaluationProjection(String id, String courseId, String courseName) {
        this.id = id;
        this.courseId = courseId;
        this.courseName = courseName;
        this.registrations = List.of();
    }
}
