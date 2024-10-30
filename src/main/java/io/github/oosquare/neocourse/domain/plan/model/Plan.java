package io.github.oosquare.neocourse.domain.plan.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.Entity;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.plan.exception.CourseSetException;
import io.github.oosquare.neocourse.domain.plan.exception.PlanException;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
@AllArgsConstructor(staticName = "createInternally")
public class Plan implements Entity {

    private final @NonNull Id id;
    private final @NonNull PlanName name;
    private @NonNull CourseSet includedCourses;

    public Plan(@NonNull Id id, @NonNull PlanName name) {
        this.id = id;
        this.name = name;
        this.includedCourses = new CourseSet();
    }

    public void includeCourse(@NonNull Course course) {
        try {
            this.includedCourses = this.includedCourses.addCourse(course.getId());
        } catch (CourseSetException exception) {
            throw new PlanException(
                String.format("Could not include Course[id=%s] to Plan[id=%s]",
                    course.getId(),
                    this.getId()
                ),
                exception
            );
        }
    }

    public void excludeCourse(@NonNull Course course) {
        try {
            this.includedCourses = this.includedCourses.removeCourse(course.getId());
        } catch (CourseSetException exception) {
            throw new PlanException(
                String.format(
                    "Could not exclude Course[id=%s] from Plan[id=%s]",
                    course,
                    this.getId()
                ),
                exception
            );
        }
    }
}
