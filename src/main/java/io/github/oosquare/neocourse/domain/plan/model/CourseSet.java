package io.github.oosquare.neocourse.domain.plan.model;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

import io.github.oosquare.neocourse.utility.id.Id;

@Value
@AllArgsConstructor(staticName = "ofInternally")
public class CourseSet {

    private final @NonNull @With Set<Id> courses;

    private CourseSet() {
        this(ImmutableSet.of());
    }

    public static CourseSet of() {
        return new CourseSet();
    }

    public CourseSet addCourse(@NonNull Id course) {
        var newCourses = ImmutableSet.<Id>builder()
            .addAll(this.courses)
            .add(course)
            .build();
        return this.withCourses(newCourses);
    }

    public CourseSet removeCourse(@NonNull Id course) {
        var newCourses = this.courses.stream()
            .filter((element) -> !element.equals(course))
            .collect(ImmutableSet.toImmutableSet());
        return this.withCourses(newCourses);
    }

    public boolean contains(@NonNull Id course) {
        return this.courses.contains(course);
    }
}
