package io.github.oosquare.neocourse.domain.plan.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.Entity;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.utility.exception.RuleViolationException;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Plan implements Entity {

    private final @NonNull Id id;
    private final @NonNull PlanName name;
    private @NonNull ClassPeriod totalClassPeriod;
    private @NonNull ClassPeriod requiredClassPeriod;
    private @NonNull CourseSet includedCourses;

    public Plan(@NonNull Id id, @NonNull PlanName name) {
        this.id = id;
        this.name = name;
        this.totalClassPeriod = ClassPeriod.of(0);
        this.requiredClassPeriod = ClassPeriod.of(0);
        this.includedCourses = CourseSet.of();
    }

    public void includeCourse(@NonNull Course course) {
        if (this.includedCourses.contains(course.getId())) {
            throw RuleViolationException.builder()
                .message("Course is already included in Plan")
                .userMessage("Course is already included in plan")
                .context("course.id", course.getId())
                .context("course.name", course.getName())
                .context("plan.id", this.getId())
                .context("plan.name", this.getName())
                .build();
        }
        this.includedCourses = this.includedCourses.addCourse(course.getId());
        this.totalClassPeriod = this.totalClassPeriod.plus(course.getClassPeriod());
    }

    public void excludeCourse(@NonNull Course course) {
        if (!this.includedCourses.contains(course.getId())) {
            throw RuleViolationException.builder()
                .message("Course is not included in Plan")
                .userMessage("Course is not included in plan")
                .context("course.id", course.getId())
                .context("course.name", course.getName())
                .context("plan.id", this.getId())
                .context("plan.name", this.getName())
                .build();
        }
        this.includedCourses = this.includedCourses.removeCourse(course.getId());
        this.totalClassPeriod = this.totalClassPeriod.minus(course.getClassPeriod());
        this.requiredClassPeriod = this.requiredClassPeriod.withUpperBound(this.totalClassPeriod);
    }

    public boolean isCourseIncluded(@NonNull Id course) {
        return this.includedCourses.contains(course);
    }

    public void assignRequiredClassPeriod(@NonNull ClassPeriod newRequiredClassPeriod) {
        if (newRequiredClassPeriod.isExceedUpperBound(this.totalClassPeriod)) {
            throw RuleViolationException.builder()
                .message("newRequiredClassPeriod should be less than totalClassPeriod")
                .userMessage("Sum of all courses' class period is not enough for the required class period")
                .context("schedule.id", id)
                .context("schedule.totalClassPeriod", this.totalClassPeriod)
                .context("newRequiredClassPeriod", newRequiredClassPeriod)
                .build();
        }
        this.requiredClassPeriod = newRequiredClassPeriod;
    }
}
