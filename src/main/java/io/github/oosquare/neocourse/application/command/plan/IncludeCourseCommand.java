package io.github.oosquare.neocourse.application.command.plan;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.utility.id.Id;

@Value
@Builder
public class IncludeCourseCommand {

    private final @NonNull Id planId;
    private final @NonNull Id courseId;
}
