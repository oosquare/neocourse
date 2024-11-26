package io.github.oosquare.neocourse.application.command.registration;

import lombok.Builder;
import lombok.Value;

import io.github.oosquare.neocourse.utility.id.Id;

@Value
@Builder
public class AddRegistrationCommand {

    private final Id scheduleId;
}
