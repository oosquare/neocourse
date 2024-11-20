package io.github.oosquare.neocourse.domain.schedule.service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.Registration;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.schedule.model.TimeRange;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.utility.exception.RuleViolationException;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    private static final ZonedDateTime TEST_BASE_TIME = ZonedDateTime.now();
    private static final Duration TEST_PERIOD = Duration.ofMinutes(45);

    private @Mock ScheduleFactory scheduleFactory;
    private @InjectMocks ScheduleService scheduleService;

    @Test
    void addNewScheduleSucceeds() {
        var course = new Course(Id.of("course0"), CourseName.of("test course"), ClassPeriod.of(1));
        var teacher = new Teacher(
            Id.of("teacher0"),
            Username.of("test-teacher"),
            DisplayedUsername.of("test teacher")
        );
        var startTime = TEST_BASE_TIME.plusMinutes(10);
        var place = Place.of("test place");
        var capacity = Capacity.of(2);

        when(this.scheduleFactory.createSchedule(any(), any(), any(), any(), any()))
            .thenReturn(createTestSchedule(false));

        var schedule = this.scheduleService.addNewSchedule(course, teacher, startTime, place, capacity);
        assertTrue(teacher.isManagingSchedule(schedule));
    }

    @Test
    void addNewScheduleThrowsStartTimeIsNotAfterNow() {
        var course = new Course(Id.of("course0"), CourseName.of("test course"), ClassPeriod.of(1));
        var teacher = new Teacher(
            Id.of("teacher0"),
            Username.of("test-teacher"),
            DisplayedUsername.of("test teacher")
        );
        var place = Place.of("test place");
        var capacity = Capacity.of(2);

        assertThrows(RuleViolationException.class, () -> {
            this.scheduleService.addNewSchedule(course, teacher, TEST_BASE_TIME, place, capacity);
        });
    }

    @Test
    void prepareRemovingScheduleSucceeds() {
        var schedule = createTestSchedule(false);
        var teacher = createTestTeacher();

        this.scheduleService.prepareRemovingSchedule(schedule, teacher);
        assertFalse(teacher.isManagingSchedule(schedule));
    }

    @Test
    void prepareRemovingScheduleThrowsWhenStartTimeIsNotAfterNow() {
        var registrations = new HashMap<Id, Registration>();
        var schedule = Schedule.builder()
            .id(Id.of("schedule0"))
            .course(Id.of("course0"))
            .teacher(Id.of("teacher0"))
            .time(TimeRange.of(TEST_BASE_TIME.minusMinutes(10), TEST_PERIOD))
            .place(Place.of("test place"))
            .capacity(Capacity.of(2))
            .registrations(registrations)
            .build();
        var teacher = createTestTeacher();

        assertThrows(RuleViolationException.class, () -> {
            this.scheduleService.prepareRemovingSchedule(schedule, teacher);
        });
    }

    @Test
    void prepareRemovingScheduleThrowsWhenScheduleHasRegistrations() {
        var schedule = createTestSchedule(true);
        var teacher = createTestTeacher();

        assertThrows(RuleViolationException.class, () -> {
            this.scheduleService.prepareRemovingSchedule(schedule, teacher);
        });
    }

    @Test
    void prepareRemovingScheduleThrowsWhenTeacherDoesNotManageSchedule() {
        var schedule = createTestSchedule(false);
        var teacher = Teacher.builder()
            .id(Id.of("teacher1"))
            .username(Username.of("test-teacher1"))
            .displayedUsername(DisplayedUsername.of("test teacher1"))
            .managedSchedules(new HashSet<>())
            .build();

        assertThrows(RuleViolationException.class, () -> {
            this.scheduleService.prepareRemovingSchedule(schedule, teacher);
        });
    }

    private static Schedule createTestSchedule(boolean withDefaultRegistrations) {
        var registrations = new HashMap<Id, Registration>();
        if (withDefaultRegistrations) {
            registrations.put(Id.of("student0"), Registration.of(Id.of("student0")));
        }
        return Schedule.builder()
            .id(Id.of("schedule0"))
            .course(Id.of("course0"))
            .teacher(Id.of("teacher0"))
            .time(TimeRange.of(TEST_BASE_TIME.plusMinutes(10), TEST_PERIOD))
            .place(Place.of("test place"))
            .capacity(Capacity.of(2))
            .registrations(registrations)
            .build();
    }

    private static Teacher createTestTeacher() {
        return Teacher.builder()
            .id(Id.of("teacher0"))
            .username(Username.of("test-teacher"))
            .displayedUsername(DisplayedUsername.of("test teacher"))
            .managedSchedules(new HashSet<>(Set.of(Id.of("schedule0"))))
            .build();
    }
}