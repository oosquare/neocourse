package io.github.oosquare.neocourse.domain.teacher.model;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

import io.github.oosquare.neocourse.domain.teacher.exception.OfferedCourseException;
import io.github.oosquare.neocourse.utility.id.Id;

@Value
@AllArgsConstructor
public class OfferedCourses {

    private final @NonNull @With Set<Id> offeredCourses;

    public OfferedCourses() {
        this(ImmutableSet.of());
    }

    public OfferedCourses addCourse(@NonNull Id course) {
        if (this.offeredCourses.contains(course)) {
            throw new OfferedCourseException(
                String.format("Could not add an already existed Course[id=%s]", course)
            );
        }
        var newOffedCourses = ImmutableSet.<Id>builder()
            .addAll(this.offeredCourses)
            .add(course)
            .build();
        return this.withOfferedCourses(newOffedCourses);
    }

    public OfferedCourses removeCourse(@NonNull Id course) {
        if (!this.offeredCourses.contains(course)) {
            throw new OfferedCourseException(
                String.format("Could not remove a not offered Course[id=%s]", course)
            );
        }
        var newOfferedCourses = this.offeredCourses.stream()
            .filter((element) -> !element.equals(course))
            .collect(ImmutableSet.toImmutableSet());
        return this.withOfferedCourses(newOfferedCourses);
    }
}
