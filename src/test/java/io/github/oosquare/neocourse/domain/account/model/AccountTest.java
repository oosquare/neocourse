package io.github.oosquare.neocourse.domain.account.model;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.id.Id;
import io.github.oosquare.neocourse.utility.exception.RuleViolationException;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    @Test
    public void addRoleSucceeds() {
        var account = createTestAccount();
        var newRole = AccountRoleKind.ADMINISTRATOR;
        account.addRole(newRole, Id.of("test-administrator"));
        assertTrue(account.hasRole(newRole));
    }

    @Test
    public void addRoleThrowsWhenRoleAlreadyExists() {
        var account = createTestAccount();
        var newRole = AccountRoleKind.TEACHER;
        assertThrows(RuleViolationException.class, () -> {
            account.addRole(newRole, account.getId());
        });
    }

    @Test
    public void addRoleThrowsWhenRoleConflictsWithExistingRoles() {
        var account = createTestAccount();
        var newRole = AccountRoleKind.STUDENT;
        assertThrows(RuleViolationException.class, () -> {
            account.addRole(newRole, Id.of("test-student"));
        });
    }

    @Test
    public void removeRoleSucceedsWhenRoleExists() {
        var account = createTestAccount();
        var roleToRemove = AccountRoleKind.TEACHER;
        account.removeRole(roleToRemove);
        assertFalse(account.hasRole(roleToRemove));
    }

    @Test
    public void removeRoleThrowsWhenRoleDoesNotExist() {
        var account = createTestAccount();
        var roleToRemove = AccountRoleKind.ADMINISTRATOR;
        assertThrows(RuleViolationException.class, () -> {
            account.removeRole(roleToRemove);
        });
    }

    private static Account createTestAccount() {
        return Account.builder()
            .id(Id.of("account-id"))
            .username(Username.of("test-account"))
            .displayedUsername(DisplayedUsername.of("Test Account"))
            .encodedPassword(EncodedPassword.of("password"))
            .role(AccountRoleKind.TEACHER, AccountRole.of(AccountRoleKind.TEACHER, Id.of("test-teacher")))
            .build();
    }
}
