package io.github.oosquare.neocourse.domain.account.model;

import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.utility.id.Id;

@Value(staticConstructor = "of")
public class AccountRole {

    private final @NonNull AccountRoleKind kind;
    private final @NonNull Id userData;
}
