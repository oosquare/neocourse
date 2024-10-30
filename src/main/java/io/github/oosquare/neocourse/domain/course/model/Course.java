package io.github.oosquare.neocourse.domain.course.model;

import java.time.Duration;

import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.Entity;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
public class Course implements Entity {

    private static final Duration PERIOD_PER_LECTURE = Duration.ofMinutes(45);
    private static final Duration PERIOD_PER_BREAK = Duration.ofMinutes(5);

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

    public Duration getActualPeriod() {
        var classPeriod = this.getClassPeriod().getValue();
        var lecturePeriod = Course.PERIOD_PER_LECTURE.multipliedBy(classPeriod);
        var breakPeriod = Course.PERIOD_PER_BREAK.multipliedBy(classPeriod - 1);
        return lecturePeriod.plus(breakPeriod);
    }
}
