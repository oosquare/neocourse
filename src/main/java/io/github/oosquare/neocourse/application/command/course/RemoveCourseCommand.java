package io.github.oosquare.neocourse.application.command.course;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.domain.account.model.Account;

@Value
@Builder
public class RemoveCourseCommand {

    private final @NonNull String courseId;
    private final @NonNull Account account;
}
