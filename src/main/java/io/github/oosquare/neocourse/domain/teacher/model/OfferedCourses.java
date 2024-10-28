package io.github.oosquare.neocourse.domain.teacher.model;

import java.util.HashSet;
import java.util.Set;

import lombok.NonNull;
import lombok.Value;
import lombok.With;

import io.github.oosquare.neocourse.domain.teacher.exception.OfferedCourseException;
import io.github.oosquare.neocourse.utility.id.Id;

@Value
public class OfferedCourses {

    private final @NonNull @With Set<Id> offeredCourses;

    public OfferedCourses() {
        this(new HashSet<>());
    }

    public OfferedCourses(@NonNull Set<Id> offeredCourses) {
        this.offeredCourses = offeredCourses;
    }

    public OfferedCourses addCourse(@NonNull Id course) {
        var newOffedCourses = new HashSet<>(this.offeredCourses);
        boolean contained = newOffedCourses.add(course);
        if (!contained) {
            throw new OfferedCourseException(
                String.format("Could not add an already existed Course[id=%s]", course)
            );
        }
        return this.withOfferedCourses(newOffedCourses);
    }

    public OfferedCourses removeCourse(@NonNull Id course) {
        var newOfferedCourses = new HashSet<>(this.offeredCourses);
        boolean contained = newOfferedCourses.remove(course);
        if (!contained) {
            throw new OfferedCourseException(
                String.format("Could not remove a not offered Course[id=%s]", course)
            );
        }
        return this.withOfferedCourses(newOfferedCourses);
    }
}
