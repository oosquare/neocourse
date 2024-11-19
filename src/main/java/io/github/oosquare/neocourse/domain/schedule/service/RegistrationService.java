package io.github.oosquare.neocourse.domain.schedule.service;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.domain.schedule.exception.RegistrationException;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.transcript.model.Transcript;
import io.github.oosquare.neocourse.utility.exception.UnreachableCodeExecutedException;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final @NonNull PlanRepository planRepository;
    private final @NonNull CourseRepository courseRepository;

    public void register(
        @NonNull Student student,
        @NonNull Schedule schedule,
        @NonNull Transcript transcript,
        @NonNull ZonedDateTime currentTime
    ) {
        var plan = this.getPlanByStudent(student);
        var course = this.getCourseBySchedule(schedule);
        this.checkCourseIncludedInPlan(student, course, plan);
        this.checkCourseSelectable(student, course, transcript);
        schedule.requestRegistration(student, currentTime);
        transcript.addCourse(course);
    }

    public void cancel(
        @NonNull Student student,
        @NonNull Schedule schedule,
        @NonNull Transcript transcript,
        @NonNull ZonedDateTime currentTime
    ) {
        var course = this.getCourseBySchedule(schedule);
        schedule.cancelRegistration(student, currentTime);
        transcript.removeCourseIfNotGraded(course);
    }

    private Plan getPlanByStudent(Student student) {
        return this.planRepository.find(student.getPlan()).orElseThrow(() ->
            UnreachableCodeExecutedException.builder()
                .message("Student's Plan should exist but it's not found")
                .context("student.id", student.getId())
                .context("student.plan", student.getPlan())
                .build());
    }

    private Course getCourseBySchedule(Schedule schedule) {
        return this.courseRepository.find(schedule.getCourse()).orElseThrow(() ->
            UnreachableCodeExecutedException.builder()
                .message("Course corresponding to Schedule should exist but it's not found")
                .context("schedule.id", schedule.getId())
                .context("schedule.course", schedule.getCourse())
                .build());
    }

    private void checkCourseIncludedInPlan(Student student, Course course, Plan plan) {
        if (!plan.isCourseIncluded(course.getId())) {
            throw new RegistrationException(String.format(
                "Course[id=%s, name=%s] is not included in Plan[id=%s] for Student[id=%s]",
                course.getId(),
                course.getName(),
                plan.getId(),
                student.getId()
            ));
        }
    }

    private void checkCourseSelectable(Student student, Course course, Transcript transcript) {
        if (!transcript.isCourseSelectable(course)) {
            throw new RegistrationException(String.format(
                "Course[id=%s, name=%s] is already selected by Student[id=%s]",
                course.getId(),
                course.getName(),
                student.getId()
            ));
        }
    }
}
