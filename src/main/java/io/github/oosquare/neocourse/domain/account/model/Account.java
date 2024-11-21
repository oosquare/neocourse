package io.github.oosquare.neocourse.domain.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.Entity;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
@AllArgsConstructor
@Builder
public class Account implements Entity {

    private final @NonNull Id id;
    private final @NonNull AccountKind kind;
    private final @NonNull Username username;
    private final @NonNull DisplayedUsername displayedUsername;
    private final @NonNull EncodedPassword encodedPassword;
    private final @NonNull Id user;

    public String toLoggingString() {
        return "Account[id=%s, username=%s, kind=%s]".formatted(id, username, kind);
    }
}
