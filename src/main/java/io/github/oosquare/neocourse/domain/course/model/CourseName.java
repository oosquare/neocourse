package io.github.oosquare.neocourse.domain.course.model;

import lombok.NonNull;
import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public class CourseName {

    private final @NonNull String courseName;

    public CourseName(@NonNull String courseName) {
        checkArgument(!courseName.isBlank(), "Course name should not be blank");
        checkArgument(
            courseName.strip().equals(courseName),
            "Course name should not have leading and trailing spaces"
        );
        this.courseName = courseName;
    }
}
