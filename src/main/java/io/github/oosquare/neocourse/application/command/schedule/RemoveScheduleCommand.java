package io.github.oosquare.neocourse.application.command.schedule;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.utility.id.Id;

@Value
@Builder
public class RemoveScheduleCommand {

    private final @NonNull Id scheduleId;
}
