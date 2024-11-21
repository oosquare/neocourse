package io.github.oosquare.neocourse.application.command.plan;

import lombok.Builder;
import lombok.Value;

import io.github.oosquare.neocourse.utility.id.Id;

@Value
@Builder
public class ExcludeCourseCommand {

    private final Id planId;
    private final Id courseId;
}
