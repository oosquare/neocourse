package io.github.oosquare.neocourse.application.command.user;

import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;

@Value
@Builder
public class ChangePasswordCommand {

    public final @NonNull @ToString.Exclude String rawOldPassword;
    public final @NonNull @ToString.Exclude EncodedPassword encodedNewPassword;
}
