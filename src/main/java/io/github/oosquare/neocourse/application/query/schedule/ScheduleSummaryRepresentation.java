package io.github.oosquare.neocourse.application.query.schedule;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.infrastructure.repository.schedule.ScheduleSummaryProjection;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class ScheduleSummaryRepresentation {

    private @NonNull String id;
    private @NonNull String courseName;
    private @NonNull String teacherDisplayedUsername;
    private @NonNull ZonedDateTime startTime;
    private @NonNull Duration period;
    private @NonNull String place;
    private @NonNull Integer capacity;

    public static ScheduleSummaryRepresentation fromData(
        @NonNull ScheduleSummaryProjection data
    ) {
        return ScheduleSummaryRepresentation.builder()
            .id(data.getId())
            .courseName(data.getCourseName())
            .teacherDisplayedUsername(data.getTeacherDisplayedUsername())
            .startTime(data.getStartTime().atZoneSameInstant(ZoneId.systemDefault()))
            .period(data.getPeriod())
            .place(data.getPlace())
            .capacity(data.getCapacity())
            .build();
    }
}
