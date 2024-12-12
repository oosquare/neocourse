package io.github.oosquare.neocourse.domain.account.model;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
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
import io.github.oosquare.neocourse.utility.exception.RuleViolationException;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Account implements Entity {

    private final @NonNull Id id;
    private final @NonNull Username username;
    private final @NonNull DisplayedUsername displayedUsername;
    private @NonNull EncodedPassword encodedPassword;
    private @NonNull @Singular Map<AccountRoleKind, AccountRole> roles;

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
        this.roles = ImmutableMap.of(kind, AccountRole.of(kind, user.getId()));
    }

    public void addRole(@NonNull AccountRoleKind roleKind, @NonNull Id userData) {
        RoleConstrainSpecification.checkRoleConflict(this, roleKind);
        if (this.roles.containsKey(roleKind)) {
            throw RuleViolationException.builder()
                .message("Role already exists")
                .userMessage("The role %s already exists for the account".formatted(roleKind))
                .context("account.id", this.id)
                .context("roleKind", roleKind)
                .build();
        }
        this.roles = ImmutableMap.<AccountRoleKind, AccountRole>builder()
            .putAll(this.roles)
            .put(roleKind, AccountRole.of(roleKind, userData))
            .build();
    }

    public void removeRole(@NonNull AccountRoleKind roleKind) {
        if (!this.roles.containsKey(roleKind)) {
            throw RuleViolationException.builder()
                .message("Role does not exist")
                .userMessage("The role %s does not exist for the account".formatted(roleKind))
                .context("account.id", this.id)
                .context("roleKind", roleKind)
                .build();
        }
        var newRoles = this.roles.entrySet()
            .stream()
            .filter(entry -> entry.getKey() != roleKind)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.roles = ImmutableMap.copyOf(newRoles);
    }

    public Set<AccountRoleKind> getRoleKinds() {
        return this.roles.keySet();
    }

    public boolean hasRole(@NonNull AccountRoleKind roleKind) {
        return this.roles.containsKey(roleKind);
    }

    public Id getUserData(@NonNull AccountRoleKind roleKind) {
        return this.roles.get(roleKind).getUserData();
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

    public void changeEncodedPassword(@NonNull EncodedPassword encodedPassword) {
        this.encodedPassword = encodedPassword;
    }
}
