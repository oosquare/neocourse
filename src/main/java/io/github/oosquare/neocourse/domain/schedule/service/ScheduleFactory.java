package io.github.oosquare.neocourse.domain.schedule.service;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.schedule.model.TimeRange;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.utility.exception.RuleViolationException;
import io.github.oosquare.neocourse.utility.id.IdGenerator;

@Service
@AllArgsConstructor
public class ScheduleFactory {

    private final @NonNull IdGenerator idGenerator;
    private final @NonNull ScheduleRepository scheduleRepository;

    public Schedule createSchedule(
        @NonNull Course course,
        @NonNull Teacher teacher,
        @NonNull ZonedDateTime startTime,
        @NonNull Place place,
        @NonNull Capacity capacity
    ) {
        var timeRange = TimeRange.of(startTime, course.getActualPeriod());
        this.checkScheduleNoConflict(timeRange, place);
        var id = this.idGenerator.generate();
        return new Schedule(id, course, teacher, startTime, place, capacity);
    }

    private void checkScheduleNoConflict(TimeRange timeRange, Place place) {
        var existedSchedules = this.scheduleRepository.findByDateAndPlace(timeRange.getStart(), place);
        var conflictSchedule = existedSchedules.stream()
            .filter(existed -> existed.getTime().hasOverlap(timeRange))
            .findFirst();
        conflictSchedule.ifPresent(existed -> {
            throw RuleViolationException.builder()
                .message("TimeRange and Place of the new Schedule is conflicted with another one")
                .userMessage("New schedule will be conflicted with existed one (%s, %s, %s)"
                    .formatted(
                        existed.getTime().getStart(),
                        existed.getTime().getPeriod(),
                        existed.getPlace().getValue()
                    ))
                .context("existed.id", existed.getId())
                .context("existed.time", existed.getTime())
                .context("existed.place", existed.getPlace())
                .context("timeRange", timeRange)
                .build();
        });
    }
}
