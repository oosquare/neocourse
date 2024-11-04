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
import io.github.oosquare.neocourse.domain.schedule.exception.EvaluationSpecificationException;
import io.github.oosquare.neocourse.domain.schedule.exception.RegistrationException;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.transcript.model.Transcript;
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
        assertThrows(RegistrationException.class, () -> {
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
        assertThrows(RegistrationException.class, () -> {
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
        assertThrows(RegistrationException.class, () -> {
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
        assertThrows(RegistrationException.class, () -> {
            schedule.cancelRegistration(
                ScheduleTest.createTestStudent(0),
                ScheduleTest.TEST_START_TIME.plusSeconds(1)
            );
        });
    }

    @Test
    void cancelRegistrationThrowsWhenStudentDoesNotExist() {
        var schedule = ScheduleTest.createTestSchedule(true);
        assertThrows(RegistrationException.class, () -> {
            schedule.cancelRegistration(
                ScheduleTest.createTestStudent(2),
                ScheduleTest.TEST_START_TIME.minusSeconds(1)
            );
        });
    }

    @Test
    void markStudentAttendedSucceeds() {
        var schedule = ScheduleTest.createTestSchedule(true);
        schedule.markStudentAttended(ScheduleTest.createTestStudent(1));
        assertEquals(
            ParticipationStatus.ATTENDED,
            schedule.getRegistrations().get(Id.of("student1")).getStatus()
        );
    }

    @Test
    void markStudentAttendedThrowsWhenStudentDoesNotExist() {
        var schedule = ScheduleTest.createTestSchedule(true);
        assertThrows(EvaluationSpecificationException.class, () -> {
            schedule.markStudentAttended(ScheduleTest.createTestStudent(2));
        });
    }

    @Test
    void markStudentAbsentSucceeds() {
        var schedule = ScheduleTest.createTestSchedule(true);
        schedule.markStudentAttended(ScheduleTest.createTestStudent(0));
        assertEquals(
            ParticipationStatus.ABSENT,
            schedule.getRegistrations().get(Id.of("student1")).getStatus()
        );
    }

    @Test
    void markStudentAbsentThrowsWhenStudentDoesNotExist() {
        var schedule = ScheduleTest.createTestSchedule(true);
        assertThrows(EvaluationSpecificationException.class, () -> {
            schedule.markStudentAbsent(ScheduleTest.createTestStudent(2));
        });
    }

    static Schedule createTestSchedule(boolean withDefaultStudents) {
        var registrations = new HashMap<Id, Registration>();

        if (withDefaultStudents) {
            registrations.put(Id.of("student0"), Registration.of(Id.of("student0")));
            registrations.put(Id.of("student1"), Registration.of(Id.of("student1")).markAbsent());
        }

        return Schedule.createInternally(
            Id.of("schedule0"),
            Id.of("course0"),
            Id.of("teacher0"),
            TimeRange.of(TEST_START_TIME, TEST_PERIOD),
            Place.of("test place"),
            Capacity.of(2),
            registrations
        );
    }

    static Student createTestStudent(int id) {
        return new Student(
            Id.of(String.format("student%d", id)),
            Username.of(String.format("student%d", id)),
            DisplayedUsername.of(String.format("student%d", id)),
            new Plan(Id.of("plan0"), PlanName.of("plan0")),
            Transcript.createInternally(Id.of("transcript0"), Id.of("plan0"), Map.of())
        );
    }
}