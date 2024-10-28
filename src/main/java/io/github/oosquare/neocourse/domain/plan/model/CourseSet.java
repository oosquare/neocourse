package io.github.oosquare.neocourse.domain.plan.model;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

import io.github.oosquare.neocourse.domain.plan.exception.CourseSetException;
import io.github.oosquare.neocourse.utility.id.Id;

@Value
@AllArgsConstructor
public class CourseSet {

    private final @NonNull @With Set<Id> courses;

    public CourseSet() {
        this(new HashSet<>());
    }

    public CourseSet addCourse(@NonNull Id course) {
        var newCourses = new HashSet<>(this.courses);
        if (!newCourses.add(course)) {
            throw new CourseSetException(
                String.format("Could not add an already existed Course[id=%s]", course)
            );
        }
        return this.withCourses(newCourses);
    }

    public CourseSet removeCourse(@NonNull Id course) {
        var newCourse = new HashSet<>(this.courses);
        if (!newCourse.remove(course)) {
            throw new CourseSetException(
                String.format("Could not remove a not Course[id=%s]", course)
            );
        }
        return this.withCourses(newCourse);
    }
}
