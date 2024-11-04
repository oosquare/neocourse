package io.github.oosquare.neocourse.domain.teacher.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.teacher.exception.EvaluationException;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.domain.transcript.model.Score;
import io.github.oosquare.neocourse.domain.transcript.model.Transcript;

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
        if (!teacher.isManagingSchedule(schedule)) {
            throw new EvaluationException(String.format(
                "Teacher[id=%s] doesn't manage Schedule[id=%s, course=%s, teacher=%s]",
                teacher.getId(),
                schedule.getId(),
                schedule.getCourse(),
                schedule.getTeacher()
            ));
        }
    }

    private void checkStudentRegistered(Student student, Schedule schedule) {
        if (!schedule.isStudentRegistered(student)) {
            throw new EvaluationException(String.format(
                "Student[id=%s] hasn't registered for Schedule[id=%s, course=%s]",
                student.getId(),
                schedule.getId(),
                schedule.getCourse()
            ));
        }
    }

    private Course getCourseBySchedule(Schedule schedule) {
        return this.courseRepository.find(schedule.getCourse()).orElseThrow(() ->
            new EvaluationException(String.format(
                "Course[id=%s] corresponding to Schedule[id=%s] should exist but it's not found",
                schedule.getCourse(),
                schedule.getId()
            )));
    }
}
