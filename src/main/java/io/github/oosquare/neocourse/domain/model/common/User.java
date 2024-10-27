package io.github.oosquare.neocourse.domain.model.common;

import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.utility.id.Id;

@Getter
public abstract class User {

    private final @NonNull Id id;
    private final @NonNull Username username;

    protected User(@NonNull Id id, @NonNull Username username) {
        this.id = id;
        this.username = username;
    }
}
