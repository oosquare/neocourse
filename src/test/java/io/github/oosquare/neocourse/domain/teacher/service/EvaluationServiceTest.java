package io.github.oosquare.neocourse.domain.teacher.service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.Registration;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.schedule.model.TimeRange;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.teacher.exception.EvaluationException;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.domain.transcript.model.Score;
import io.github.oosquare.neocourse.domain.transcript.model.Transcript;
import io.github.oosquare.neocourse.domain.transcript.model.TranscriptItem;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceTest {

    private @Mock CourseRepository courseRepository;
    private @InjectMocks EvaluationService evaluationService;

    @Test
    void gradeStudentSucceeds() {
        when(this.courseRepository.find(Id.of("course0")))
            .thenReturn(Optional.of(createTestCourse()));

        var teacher = createTestTeacher();
        var schedule = createTestSchedule(0);
        var student = createTestStudent();
        var transcript = createTestTranscript();

        this.evaluationService.gradeStudent(teacher, schedule, student, transcript, Score.of(90));
        assertEquals(Optional.of(Score.of(90)), transcript.getScore(createTestCourse()));
    }

    @Test
    void gradeStudentThrowsWhenTeacherDoesNotManageSchedule() {
        var teacher = Teacher.createInternally(
            Id.of("teacher0"),
            Username.of("teacher0"),
            DisplayedUsername.of("teacher 0"),
            new HashSet<>()
        );
        var schedule = createTestSchedule(1);
        var student = createTestStudent();
        var transcript = createTestTranscript();

        assertThrows(EvaluationException.class, () -> {
            this.evaluationService.gradeStudent(teacher, schedule, student, transcript, Score.of(90));
        });
    }

    @Test
    void gradeStudentThrowsWhenStudentIsNotRegistered() {
        var teacher = createTestTeacher();
        var schedule = createTestSchedule(1);
        var student = Student.createInternally(
            Id.of("student1"),
            Username.of("student1"),
            DisplayedUsername.of("student 1"),
            Id.of("plan0"),
            Id.of("transcript0")
        );
        var transcript = createTestTranscript();

        assertThrows(EvaluationException.class, () -> {
            this.evaluationService.gradeStudent(teacher, schedule, student, transcript, Score.of(90));
        });
    }

    @Test
    void gradeStudentThrowsWhenCourseIsNotFound() {
        var teacher = createTestTeacher();
        var schedule = createTestSchedule(0);
        var student = createTestStudent();
        var transcript = createTestTranscript();

        assertThrows(EvaluationException.class, () -> {
            this.evaluationService.gradeStudent(teacher, schedule, student, transcript, Score.of(90));
        });
    }

    @Test
    void markStudentAbsentSucceeds() {
        when(this.courseRepository.find(Id.of("course0")))
            .thenReturn(Optional.of(createTestCourse()));

        var teacher = createTestTeacher();
        var schedule = createTestSchedule(0);
        var student = createTestStudent();
        var transcript = createTestTranscript();

        this.evaluationService.markStudentAbsent(teacher, schedule, student, transcript);
        assertEquals(Optional.of(Score.of(0)), transcript.getScore(createTestCourse()));
    }

    private static Teacher createTestTeacher() {
        return Teacher.createInternally(
            Id.of("teacher0"),
            Username.of("teacher0"),
            DisplayedUsername.of("teacher 0"),
            new HashSet<>(Set.of(Id.of("schedule0")))
        );
    }

    private static Student createTestStudent() {
        return Student.createInternally(
            Id.of("student0"),
            Username.of("student0"),
            DisplayedUsername.of("student 0"),
            Id.of("plan0"),
            Id.of("transcript0")
        );
    }

    private static Schedule createTestSchedule(int teacherId) {
        var registration = new HashMap<Id, Registration>();
        registration.put(Id.of("student0"), Registration.of(Id.of("student0")));
        return Schedule.createInternally(
            Id.of("schedule0"),
            Id.of("course0"),
            Id.of(String.format("teacher%d", teacherId)),
            TimeRange.of(ZonedDateTime.now(), Duration.ofMinutes(45)),
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

    private static Transcript createTestTranscript() {
        return Transcript.createInternally(
            Id.of("transcript0"),
            Id.of("plan0"),
            new HashMap<>(Map.of(
                Id.of("course0"),
                TranscriptItem.of(Id.of("course0"), ClassPeriod.of(1))
            ))
        );
    }
}