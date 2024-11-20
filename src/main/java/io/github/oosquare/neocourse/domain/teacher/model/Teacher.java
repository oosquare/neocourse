package io.github.oosquare.neocourse.domain.teacher.model;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.User;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
@Builder
public class Teacher implements User {

    private final @NonNull Id id;
    private final @NonNull Username username;
    private final @NonNull DisplayedUsername displayedUsername;

    public Teacher(
        @NonNull Id id,
        @NonNull Username username,
        @NonNull DisplayedUsername displayedUsername
    ) {
        this.id = id;
        this.username = username;
        this.displayedUsername = displayedUsername;
    }
}
