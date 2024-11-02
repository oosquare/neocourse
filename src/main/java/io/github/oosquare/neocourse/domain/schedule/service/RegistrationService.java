package io.github.oosquare.neocourse.domain.schedule.service;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.domain.schedule.exception.RegistrationException;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.student.model.Student;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final @NonNull PlanRepository planRepository;

    public void register(
        @NonNull Student student,
        @NonNull Schedule schedule,
        @NonNull ZonedDateTime currentTime
    ) {
        var plan = this.getPlanForStudent(student);
        this.checkCourseIncludedInPlan(student, schedule, plan);
        schedule.requestRegistration(student, currentTime);
    }

    public void cancel(
        @NonNull Student student,
        @NonNull Schedule schedule,
        @NonNull ZonedDateTime currentTime
    ) {
        schedule.cancelRegistration(student, currentTime);
    }

    private Plan getPlanForStudent(Student student) {
        return planRepository.find(student.getPlan()).orElseThrow(() ->
            new RegistrationException(String.format(
                "Student[id=%s]'s Plan[id=%s] should exist but it's not found",
                student.getId(),
                student.getPlan()
            )));
    }

    private void checkCourseIncludedInPlan(Student student, Schedule schedule, Plan plan) {
        if (!plan.isCourseIncluded(schedule.getCourse())) {
            throw new RegistrationException(String.format(
                "Schedule[id=%s, course=%s] is not selectable for Student[id=%s]",
                schedule.getId(),
                schedule.getCourse(),
                student.getId()
            ));
        }
    }
}
