package io.github.oosquare.neocourse.infrastructure.repository.schedule;

import java.time.Duration;
import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScheduleSummaryProjection {

    private String id;
    private String courseName;
    private String teacherDisplayedUsername;
    private OffsetDateTime startTime;
    private Duration period;
    private String place;
    private Integer capacity;
}
