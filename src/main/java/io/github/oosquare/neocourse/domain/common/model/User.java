package io.github.oosquare.neocourse.domain.common.model;

import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.utility.id.Id;

@Getter
public abstract class User {

    private final @NonNull Id id;
    private final @NonNull Username username;
    private final @NonNull DisplayedUsername displayedUsername;

    protected User(
        @NonNull Id id,
        @NonNull Username username,
        @NonNull DisplayedUsername displayedUsername
    ) {
        this.id = id;
        this.username = username;
        this.displayedUsername = displayedUsername;
    }
}
