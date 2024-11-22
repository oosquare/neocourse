package io.github.oosquare.neocourse.application.command.schedule;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.utility.id.Id;

@Value
@Builder
public class AddScheduleCommand {

    private final @NonNull Id courseId;
    private final @NonNull ZonedDateTime startTime;
    private final @NonNull Place place;
    private final @NonNull Capacity capacity;
}
