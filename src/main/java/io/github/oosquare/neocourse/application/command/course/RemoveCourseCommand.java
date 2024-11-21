package io.github.oosquare.neocourse.application.command.course;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.utility.id.Id;

@Value
@Builder
public class RemoveCourseCommand {

    private final @NonNull Id courseId;
}
