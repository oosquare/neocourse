package io.github.oosquare.neocourse.domain.schedule.service;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.utility.exception.RuleViolationException;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final @NonNull ScheduleFactory scheduleFactory;

    public Schedule addNewSchedule(
        @NonNull Course course,
        @NonNull Teacher teacher,
        @NonNull ZonedDateTime startTime,
        @NonNull Place place,
        @NonNull Capacity capacity
    ) {
        this.checkStartTimeIsAfterNow(startTime);
        var schedule = this.scheduleFactory.createSchedule(course, teacher, startTime, place, capacity);
        teacher.addManagedSchedule(schedule);
        return schedule;
    }

    public void prepareRemovingSchedule(@NonNull Schedule schedule, @NonNull Teacher teacher) {
        this.checkStartTimeIsAfterNow(schedule.getTime().getStart());
        this.checkScheduleHasNoRegistration(schedule);
        this.checkTeacherManagesSchedule(teacher, schedule);
        teacher.removeManagedSchedule(schedule);
    }

    private void checkStartTimeIsAfterNow(ZonedDateTime startTime) {
        var now = ZonedDateTime.now();
        if (!startTime.isAfter(now)) {
            throw RuleViolationException.builder()
                .message("Schedule already started can't be created or modified")
                .userMessage("Could not create or operate on a schedule which already started at %s"
                    .formatted(startTime))
                .context("startTime", startTime)
                .context("now", now)
                .build();
        }
    }

    private void checkScheduleHasNoRegistration(Schedule schedule) {
        if (!schedule.getRegistrations().isEmpty()) {
            throw RuleViolationException.builder()
                .message("Schedule with registrations associated can't be removed")
                .userMessage("Could not remove this schedule before canceling all associated registrations")
                .context("schedule.id", schedule.getId())
                .build();
        }
    }

    private void checkTeacherManagesSchedule(Teacher teacher, Schedule schedule) {
        if (!teacher.isManagingSchedule(schedule)) {
            throw RuleViolationException.builder()
                .message("Teacher who doesn't manage this Schedule can't operate on it")
                .userMessage("Teacher doesn't manage this schedule. Operations not permitted.")
                .context("teacher.id", teacher.getId())
                .context("teacher.username", teacher.getUsername())
                .context("schedule.id", schedule.getId())
                .context("schedule.teacher", schedule.getTeacher())
                .build();
        }
    }
}
