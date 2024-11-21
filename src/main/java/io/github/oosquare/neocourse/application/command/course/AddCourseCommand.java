package io.github.oosquare.neocourse.application.command.course;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.CourseName;

@Value
@Builder
public class AddCourseCommand {

    private final @NonNull CourseName courseName;
    private final @NonNull ClassPeriod classPeriod;
}
