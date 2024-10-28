package io.github.oosquare.neocourse.domain.plan.model;

import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.plan.exception.CourseSetException;
import io.github.oosquare.neocourse.domain.plan.exception.PlanException;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
public class Plan {

    private final @NonNull Id id;
    private final @NonNull PlanName name;
    private @NonNull CourseSet includedCourses;

    public Plan(@NonNull Id id, @NonNull PlanName name) {
        this.id = id;
        this.name = name;
        this.includedCourses = new CourseSet();
    }

    public void includeCourse(@NonNull Id course) {
        try {
            this.includedCourses = this.includedCourses.addCourse(course);
        } catch (CourseSetException exception) {
            throw new PlanException(
                String.format("Could not include Course[id=%s] to Plan[id=%s]", course, this.getId()),
                exception
            );
        }
    }

    public void excludeCourse(@NonNull Id course) {
        try {
            this.includedCourses = this.includedCourses.removeCourse(course);
        } catch (CourseSetException exception) {
            throw new PlanException(
                String.format("Could not exclude Course[id=%s] from Plan[id=%s]", course, this.getId()),
                exception
            );
        }
    }
}
