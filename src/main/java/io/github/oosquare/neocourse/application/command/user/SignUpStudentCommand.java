package io.github.oosquare.neocourse.application.command.user;

import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.id.Id;

@Value
@Builder
@ToString
public class SignUpStudentCommand {

    private final @NonNull Username username;
    private final @NonNull DisplayedUsername displayedUsername;
    private final @NonNull Id planId;
    private final @NonNull @ToString.Exclude EncodedPassword encodedPassword;
}
