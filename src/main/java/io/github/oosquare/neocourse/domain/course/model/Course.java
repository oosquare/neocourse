package io.github.oosquare.neocourse.domain.course.model;

import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.utility.id.Id;

@Getter
public class Course {

    private final @NonNull Id id;
    private final @NonNull CourseName name;
    private final @NonNull ClassPeriod classPeriod;

    public Course(
        @NonNull Id id,
        @NonNull CourseName name,
        @NonNull ClassPeriod classPeriod
    ) {
        this.id = id;
        this.name = name;
        this.classPeriod = classPeriod;
    }
}
