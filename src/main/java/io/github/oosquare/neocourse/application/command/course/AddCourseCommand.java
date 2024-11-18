package io.github.oosquare.neocourse.application.command.course;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.domain.account.model.Account;

@Value
@Builder
public class AddCourseCommand {

    private final @NonNull String courseName;
    private final @NonNull Integer classPeriod;
    private final @NonNull Account account;
}
