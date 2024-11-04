package io.github.oosquare.neocourse.domain.schedule.service;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.schedule.exception.ScheduleException;
import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;

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
            throw new ScheduleException(String.format(
                "Could not create or operate on schedule starting before now (%s)",
                now
            ));
        }
    }

    private void checkScheduleHasNoRegistration(Schedule schedule) {
        if (!schedule.getRegistrations().isEmpty()) {
            throw new ScheduleException(String.format(
                "Could not remove Schedule[id=%s, course=%s] before canceling all associated registrations",
                schedule.getId(),
                schedule.getCourse()
            ));
        }
    }

    private void checkTeacherManagesSchedule(Teacher teacher, Schedule schedule) {
        if (!teacher.isManagingSchedule(schedule)) {
            throw new ScheduleException(String.format(
                "Teacher[id=%s] doesn't manage Schedule[id=%s, course=%s, teacher=%s]",
                teacher.getId(),
                schedule.getId(),
                schedule.getCourse(),
                schedule.getTeacher()
            ));
        }
    }
}
