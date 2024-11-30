package io.github.oosquare.neocourse.domain.account.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import io.github.oosquare.neocourse.domain.Entity;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.User;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Account implements Entity {

    private final @NonNull Id id;
    private final @NonNull Username username;
    private final @NonNull DisplayedUsername displayedUsername;
    private final @NonNull EncodedPassword encodedPassword;
    private final @NonNull @Singular Map<AccountRoleKind, AccountRole> roles;

    public Account(
        @NonNull Id id,
        @NonNull AccountRoleKind kind,
        @NonNull User user,
        @NonNull EncodedPassword encodedPassword
    ) {
        this.id = id;
        this.username = user.getUsername();
        this.displayedUsername = user.getDisplayedUsername();
        this.encodedPassword = encodedPassword;
        this.roles = new EnumMap<>(AccountRoleKind.class);
        this.roles.put(kind, AccountRole.of(kind, user.getId()));
    }

    public Set<AccountRoleKind> getRoleKinds() {
        return Collections.unmodifiableSet(this.roles.keySet());
    }

    public boolean hasRole(@NonNull AccountRoleKind roleKind) {
        return this.roles.containsKey(roleKind);
    }

    public String toLoggingString() {
        return "Account[id=%s, username=%s, kind=%s]".formatted(id, username, this.roleKindsToString());
    }

    public String roleKindsToString() {
        return this.roles.keySet()
            .stream()
            .map(AccountRoleKind::toString)
            .reduce((lhs, rhs) -> lhs + "+" + rhs)
            .orElse("NONE");
    }
}
