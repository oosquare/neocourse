package io.github.oosquare.neocourse.domain.schedule.model;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.transcript.model.Transcript;
import io.github.oosquare.neocourse.utility.exception.RuleViolationException;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    private static final ZonedDateTime TEST_START_TIME = ZonedDateTime.now();
    private static final Duration TEST_PERIOD = Duration.ofMinutes(45);

    @Test
    void requestRegistrationSucceeds() {
        var schedule = ScheduleTest.createTestSchedule(false);
        schedule.requestRegistration(
            ScheduleTest.createTestStudent(0),
            ScheduleTest.TEST_START_TIME.minusSeconds(1)
        );
        assertTrue(schedule.getRegistrations().containsKey(Id.of("student0")));
    }

    @Test
    void requestRegistrationThrowsWhenScheduleIsFrozen() {
        assertThrows(RuleViolationException.class, () -> {
            var schedule = ScheduleTest.createTestSchedule(false);
            schedule.requestRegistration(
                ScheduleTest.createTestStudent(0),
                ScheduleTest.TEST_START_TIME.plusSeconds(1)
            );
        });
    }

    @Test
    void requestRegistrationThrowsWhenScheduleIsFull() {
        var schedule = ScheduleTest.createTestSchedule(false);
        schedule.requestRegistration(
            ScheduleTest.createTestStudent(0),
            ScheduleTest.TEST_START_TIME.minusSeconds(1)
        );
        schedule.requestRegistration(
            ScheduleTest.createTestStudent(1),
            ScheduleTest.TEST_START_TIME.minusSeconds(1)
        );
        assertThrows(RuleViolationException.class, () -> {
            schedule.requestRegistration(
                ScheduleTest.createTestStudent(2),
                ScheduleTest.TEST_START_TIME.minusSeconds(1)
            );
        });
    }

    @Test
    void requestRegistrationThrowsWhenStudentExists() {
        var schedule = ScheduleTest.createTestSchedule(false);
        schedule.requestRegistration(
            ScheduleTest.createTestStudent(0),
            ScheduleTest.TEST_START_TIME.minusSeconds(1)
        );
        assertThrows(RuleViolationException.class, () -> {
            schedule.requestRegistration(
                ScheduleTest.createTestStudent(0),
                ScheduleTest.TEST_START_TIME.minusSeconds(1)
            );
        });
    }

    @Test
    void cancelRegistrationSucceeds() {
        var schedule = ScheduleTest.createTestSchedule(true);
        schedule.cancelRegistration(
            ScheduleTest.createTestStudent(0),
            ScheduleTest.TEST_START_TIME.minusSeconds(1)
        );
    }

    @Test
    void cancelRegistrationThrowsWhenScheduleIsFrozen() {
        var schedule = ScheduleTest.createTestSchedule(true);
        assertThrows(RuleViolationException.class, () -> {
            schedule.cancelRegistration(
                ScheduleTest.createTestStudent(0),
                ScheduleTest.TEST_START_TIME.plusSeconds(1)
            );
        });
    }

    @Test
    void cancelRegistrationThrowsWhenStudentDoesNotExist() {
        var schedule = ScheduleTest.createTestSchedule(true);
        assertThrows(RuleViolationException.class, () -> {
            schedule.cancelRegistration(
                ScheduleTest.createTestStudent(2),
                ScheduleTest.TEST_START_TIME.minusSeconds(1)
            );
        });
    }

    @Test
    void markStudentAttended() {
        var schedule = ScheduleTest.createTestSchedule(true);
        schedule.markStudentAttended(ScheduleTest.createTestStudent(1));
        assertEquals(
            ParticipationStatus.ATTENDED,
            schedule.getRegistrations().get(Id.of("student1")).getStatus()
        );
    }

    @Test
    void markStudentAbsent() {
        var schedule = ScheduleTest.createTestSchedule(true);
        schedule.markStudentAbsent(ScheduleTest.createTestStudent(0));
        assertEquals(
            ParticipationStatus.ABSENT,
            schedule.getRegistrations().get(Id.of("student0")).getStatus()
        );
    }

    static Schedule createTestSchedule(boolean withDefaultStudents) {
        var registrations = new HashMap<Id, Registration>();

        if (withDefaultStudents) {
            registrations.put(Id.of("student0"), Registration.of(Id.of("student0")));
            registrations.put(Id.of("student1"), Registration.of(Id.of("student1")).markAbsent());
        }

        return Schedule.builder()
            .id(Id.of("schedule0"))
            .course(Id.of("course0"))
            .teacher(Id.of("teacher0"))
            .time(TimeRange.of(TEST_START_TIME, TEST_PERIOD))
            .place(Place.of("test place"))
            .capacity(Capacity.of(2))
            .registrations(registrations)
            .build();
    }

    static Student createTestStudent(int id) {
        return new Student(
            Id.of(String.format("student%d", id)),
            Username.of(String.format("student%d", id)),
            DisplayedUsername.of(String.format("student%d", id)),
            new Plan(Id.of("plan0"), PlanName.of("plan0")),
            Transcript.builder()
                .id(Id.of("transcript0"))
                .plan(Id.of("plan0"))
                .courseScores(Map.of())
                .build()
        );
    }
}