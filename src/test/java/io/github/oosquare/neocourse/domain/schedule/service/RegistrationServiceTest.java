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
import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.Registration;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.schedule.model.TimeRange;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.transcript.model.Transcript;
import io.github.oosquare.neocourse.utility.exception.RuleViolationException;
import io.github.oosquare.neocourse.utility.exception.UnreachableCodeExecutedException;
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
        assertFalse(transcript.isCourseSelectable(createTestCourse()));
    }

    @Test
    void registerThrowsWhenPlanDoesNotExist() {
        when(this.planRepository.find(Id.of("plan0")))
            .thenReturn(Optional.empty());

        var student = createTestStudent(0);
        var schedule = createTestSchedule(false);
        var transcript = createTestTranscript(0);

        assertThrows(UnreachableCodeExecutedException.class, () -> {
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
        var plan = Plan.builder()
            .id(Id.of("plan0"))
            .name(PlanName.of("plan 0"))
            .includedCourses(CourseSet.ofInternally(Set.of(Id.of("course1"))))
            .build();
        when(this.planRepository.find(Id.of("plan0")))
            .thenReturn(Optional.of(plan));
        when(this.courseRepository.find(Id.of("course0")))
            .thenReturn(Optional.of(createTestCourse()));

        var student = createTestStudent(0);
        var schedule = createTestSchedule(false);
        var transcript = createTestTranscript(0);

        assertThrows(RuleViolationException.class, () -> {
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
        assertTrue(transcript.isCourseSelectable(createTestCourse()));
    }

    private static Plan createTestPlan() {
        return Plan.builder()
            .id(Id.of("plan0"))
            .name(PlanName.of("plan 0"))
            .includedCourses(CourseSet.ofInternally(Set.of(Id.of("course0"))))
            .build();
    }

    private static Student createTestStudent(int id) {
        return Student.builder()
            .id(Id.of(String.format("student%d", id)))
            .username(Username.of(String.format("student%d", id)))
            .displayedUsername(DisplayedUsername.of(String.format("student %d", id)))
            .plan(Id.of("plan0"))
            .transcript(Id.of("transcript0"))
            .build();
    }

    private static Schedule createTestSchedule(boolean withDefaultStudent) {
        var registration = new HashMap<Id, Registration>();
        if (withDefaultStudent) {
            registration.put(Id.of("student0"), Registration.of(Id.of("student0")));
            registration.put(Id.of("student1"), Registration.of(Id.of("student1")));
        }
        return Schedule.builder()
            .id(Id.of("schedule0"))
            .course(Id.of("course0"))
            .teacher(Id.of("teacher0"))
            .time(TimeRange.of(BASE_TIME, Duration.ofMinutes(45)))
            .place(Place.of("place0"))
            .capacity(Capacity.of(2))
            .registrations(registration)
            .build();
    }

    private static Course createTestCourse() {
        return Course.builder()
            .id(Id.of("course0"))
            .name(CourseName.of("course 0"))
            .classPeriod(ClassPeriod.of(1))
            .build();
    }

    private static Transcript createTestTranscript(int id) {
        return Transcript.builder()
            .id(Id.of(String.format("transcript%d", id)))
            .plan(Id.of(String.format("plan%d", id)))
            .courseScores(new HashMap<>())
            .build();
    }
}