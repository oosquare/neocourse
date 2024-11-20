package io.github.oosquare.neocourse.domain.course.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.domain.schedule.service.ScheduleRepository;
import io.github.oosquare.neocourse.utility.exception.RuleViolationException;

@Service
@AllArgsConstructor
public class CourseService {

    private final @NonNull PlanRepository planRepository;
    private final @NonNull ScheduleRepository scheduleRepository;
    private final @NonNull CourseFactory courseFactory;

    public Course addCourse(@NonNull CourseName courseName, @NonNull ClassPeriod classPeriod) {
        return this.courseFactory.createCourse(courseName, classPeriod);
    }

    public void prepareRemovingCourse(@NonNull Course course) {
        this.checkNotIncludedInPlan(course);
        this.checkNotAssociatedWithSchedule(course);
    }

    private void checkNotIncludedInPlan(Course course) {
        this.planRepository.findByIncludedCourse(course).ifPresent(plan -> {
            throw RuleViolationException.builder()
                .message("Course included in another Plan can't be removed")
                .userMessage("Course is included in plan %s. Please exclude it from the plan first"
                    .formatted(plan.getName().getValue()))
                .context("course.id", course.getId())
                .context("course.name", course.getName())
                .context("plan.id", plan.getId())
                .context("plan.name", plan.getName())
                .build();
        });
    }

    private void checkNotAssociatedWithSchedule(Course course) {
        this.scheduleRepository.findByCourse(course).ifPresent(schedule -> {
            throw RuleViolationException.builder()
                .message("Course associated with another Schedule can't be removed")
                .userMessage("Course is associated with a schedule (%s, %s). Please cancel the schedule first"
                    .formatted(schedule.getTime().getStart(), schedule.getPlace().getValue()))
                .context("course.id", course.getId())
                .context("course.name", course.getName())
                .context("schedule.id", course.getId())
                .context("schedule.time.start", schedule.getTime().getStart())
                .context("schedule.place", schedule.getPlace())
                .build();
        });
    }
}
