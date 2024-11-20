package io.github.oosquare.neocourse.domain.teacher.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.domain.transcript.model.Score;
import io.github.oosquare.neocourse.domain.transcript.model.Transcript;
import io.github.oosquare.neocourse.utility.exception.RuleViolationException;
import io.github.oosquare.neocourse.utility.exception.UnreachableCodeExecutedException;

@Service
@AllArgsConstructor
public class EvaluationService {

    private final @NonNull CourseRepository courseRepository;

    public void gradeStudent(
        @NonNull Teacher teacher,
        @NonNull Schedule schedule,
        @NonNull Student student,
        @NonNull Transcript transcript,
        @NonNull Score score
    ) {
        this.checkTeacherManagesSchedule(teacher, schedule);
        this.checkStudentRegistered(student, schedule);
        schedule.markStudentAttended(student);
        var course = this.getCourseBySchedule(schedule);
        transcript.gradeCourse(course, score);
    }

    public void markStudentAbsent(
        @NonNull Teacher teacher,
        @NonNull Schedule schedule,
        @NonNull Student student,
        @NonNull Transcript transcript
    ) {
        this.checkTeacherManagesSchedule(teacher, schedule);
        this.checkStudentRegistered(student, schedule);
        schedule.markStudentAbsent(student);
        var course = this.getCourseBySchedule(schedule);
        transcript.markAbsent(course);
    }

    private void checkTeacherManagesSchedule(Teacher teacher, Schedule schedule) {
        if (!schedule.getTeacher().equals(teacher.getId())) {
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

    private void checkStudentRegistered(Student student, Schedule schedule) {
        if (!schedule.isStudentRegistered(student)) {
            throw RuleViolationException.builder()
                .message("Student with no registration for this Schedule can't be graded")
                .userMessage("Student hasn't registered for this schedule. Can't grade for this student")
                .context("student.id", student.getId())
                .context("student.username", student.getUsername())
                .context("schedule.id", schedule.getId())
                .build();
        }
    }

    private Course getCourseBySchedule(Schedule schedule) {
        return this.courseRepository.find(schedule.getCourse()).orElseThrow(() ->
            UnreachableCodeExecutedException.builder()
                .message("Course corresponding to Schedule should exist but it's not found")
                .context("schedule.id", schedule.getId())
                .context("schedule.course", schedule.getCourse())
                .build());
    }
}
