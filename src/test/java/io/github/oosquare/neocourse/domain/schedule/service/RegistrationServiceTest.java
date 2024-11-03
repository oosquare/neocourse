package io.github.oosquare.neocourse.domain.schedule.service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Optional;
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
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.plan.model.CourseSet;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.domain.schedule.exception.RegistrationException;
import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.Registration;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.schedule.model.TimeRange;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.transcript.model.Transcript;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    private static final ZonedDateTime BASE_TIME = ZonedDateTime.ofInstant(
        Instant.ofEpochSecond(10000),
        ZoneId.of("Z")
    );

    private @Mock PlanRepository planRepository;
    private @Mock CourseRepository courseRepository;
    private @InjectMocks RegistrationService registrationService;

    @Test
    void registerSucceeds() {
        when(this.planRepository.find(Id.of("plan0")))
            .thenReturn(Optional.of(createTestPlan()));
        when(this.courseRepository.find(Id.of("course0")))
            .thenReturn(Optional.of(createTestCourse()));

        var student = createTestStudent(0);
        var schedule = createTestSchedule(false);
        var transcript = createTestTranscript(0);

        this.registrationService.register(
            student,
            schedule,
            transcript,
            BASE_TIME.minusSeconds(10)
        );
        assertTrue(schedule.isStudentRegistered(student));
        assertTrue(transcript.isCourseAdded(createTestCourse()));
    }

    @Test
    void registerThrowsWhenPlanDoesNotExist() {
        when(this.planRepository.find(Id.of("plan0")))
            .thenReturn(Optional.empty());

        var student = createTestStudent(0);
        var schedule = createTestSchedule(false);
        var transcript = createTestTranscript(0);

        assertThrows(RegistrationException.class, () -> {
            this.registrationService.register(
                student,
                schedule,
                transcript,
                BASE_TIME.minusSeconds(10)
            );
        });
    }

    @Test
    void registerThrowsWhenCourseIsNotIncludedInPlan() {
        when(this.planRepository.find(Id.of("plan0")))
            .thenReturn(Optional.of(Plan.createInternally(
                Id.of("plan0"),
                PlanName.of("plan 0"),
                CourseSet.ofInternally(Set.of(Id.of("course1")))
            )));
        when(this.courseRepository.find(Id.of("course0")))
            .thenReturn(Optional.of(createTestCourse()));

        var student = createTestStudent(0);
        var schedule = createTestSchedule(false);
        var transcript = createTestTranscript(0);

        assertThrows(RegistrationException.class, () -> {
            this.registrationService.register(
                student,
                schedule,
                transcript,
                BASE_TIME.minusSeconds(10)
            );
        });
    }

    @Test
    void cancelSucceeds() {
        when(this.courseRepository.find(Id.of("course0")))
            .thenReturn(Optional.of(createTestCourse()));

        var student = createTestStudent(1);
        var schedule = createTestSchedule(true);
        var transcript = createTestTranscript(1);

        this.registrationService.cancel(
            student,
            schedule,
            transcript,
            BASE_TIME.minusSeconds(10)
        );
        assertFalse(schedule.isStudentRegistered(student));
        assertFalse(transcript.isCourseAdded(createTestCourse()));
    }

    private static Plan createTestPlan() {
        return Plan.createInternally(
            Id.of("plan0"),
            PlanName.of("plan 0"),
            CourseSet.ofInternally(Set.of(Id.of("course0")))
        );
    }

    private static Student createTestStudent(int id) {
        return Student.createInternally(
            Id.of(String.format("student%d", id)),
            Username.of(String.format("student%d", id)),
            DisplayedUsername.of(String.format("student %d", id)),
            Id.of("plan0"),
            Id.of("transcript0")
        );
    }

    private static Schedule createTestSchedule(boolean withDefaultStudent) {
        var registration = new HashMap<Id, Registration>();
        if (withDefaultStudent) {
            registration.put(Id.of("student0"), Registration.of(Id.of("student0")));
            registration.put(Id.of("student1"), Registration.of(Id.of("student1")));
        }
        return Schedule.createInternally(
            Id.of("schedule0"),
            Id.of("course0"),
            Id.of("teacher0"),
            TimeRange.of(BASE_TIME, Duration.ofMinutes(45)),
            Place.of("place0"),
            Capacity.of(2),
            registration
        );
    }

    private static Course createTestCourse() {
        return Course.createInternally(
            Id.of("course0"),
            CourseName.of("course 0"),
            ClassPeriod.of(1)
        );
    }

    private static Transcript createTestTranscript(int id) {
        return Transcript.createInternally(
            Id.of(String.format("transcript%d", id)),
            Id.of(String.format("plan%d", id)),
            new HashMap<>()
        );
    }
}