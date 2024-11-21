package io.github.oosquare.neocourse.domain.plan.model;

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
@AllArgsConstructor
@Builder
public class Plan implements Entity {

    private final @NonNull Id id;
    private final @NonNull PlanName name;
    private @NonNull ClassPeriod requiredClassPeriod;
    private @NonNull CourseSet includedCourses;

    public Plan(@NonNull Id id, @NonNull PlanName name) {
        this.id = id;
        this.name = name;
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
        this.requiredClassPeriod = this.requiredClassPeriod.plus(course.getClassPeriod());
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
        this.requiredClassPeriod = this.requiredClassPeriod.minus(course.getClassPeriod());
    }

    public boolean isCourseIncluded(@NonNull Id course) {
        return this.includedCourses.contains(course);
    }
}
