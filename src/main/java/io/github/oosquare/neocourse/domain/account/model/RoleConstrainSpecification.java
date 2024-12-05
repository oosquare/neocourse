package io.github.oosquare.neocourse.domain.account.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.EnumMap;

import com.google.common.collect.Sets;
import lombok.NonNull;

import io.github.oosquare.neocourse.utility.exception.RuleViolationException;

public class RoleConstrainSpecification {

    private static final Map<AccountRoleKind, Set<AccountRoleKind>> conflictingRoles
        = new EnumMap<>(AccountRoleKind.class);

    static {
        addConflict(AccountRoleKind.STUDENT, AccountRoleKind.TEACHER);
        addConflict(AccountRoleKind.STUDENT, AccountRoleKind.ADMINISTRATOR);
    }

    private static void addConflict(AccountRoleKind role1, AccountRoleKind role2) {
        conflictingRoles.computeIfAbsent(role1, k -> new HashSet<>()).add(role2);
        conflictingRoles.computeIfAbsent(role2, k -> new HashSet<>()).add(role1);
    }

    public static void checkRoleConflict(@NonNull Account account, @NonNull AccountRoleKind newRole) {
        Optional.ofNullable(conflictingRoles.get(newRole)).ifPresent(roles -> {
            var conflicts = Sets.intersection(roles, account.getRoleKinds());
            if (!conflicts.isEmpty()) {
                throw RuleViolationException.builder()
                    .message("Role conflict detected")
                    .userMessage("The role %s cannot be assigned because it conflicts with an existing role."
                        .formatted(newRole))
                    .context("account.id", account.getId())
                    .context("roleKind", newRole)
                    .context("conflicts", conflicts)
                    .build();
            }
        });
    }
}