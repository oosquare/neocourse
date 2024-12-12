package io.github.oosquare.neocourse.application.query.account;

import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.infrastructure.repository.account.AccountData;
import io.github.oosquare.neocourse.infrastructure.repository.account.AccountRoleData;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class AccountDetailedRepresentation {

    private final @NonNull String id;
    private final @NonNull String username;
    private final @NonNull String displayedUsername;
    private final @NonNull String roles;

    public static AccountDetailedRepresentation fromData(
        @NonNull AccountData data
    ) {
        return AccountDetailedRepresentation.builder()
            .id(data.getId())
            .username(data.getUsername())
            .displayedUsername(data.getDisplayedUsername())
            .roles(rolesToString(data.getRoles()))
            .build();
    }

    private static String rolesToString(Set<AccountRoleData> data) {
        return data.stream()
            .map(AccountRoleData::getRoleKind)
            .map(Object::toString)
            .map(String::toLowerCase)
            .reduce((a, b) -> a + ", " + b)
            .orElse("none");
    }
}
