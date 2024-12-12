package io.github.oosquare.neocourse.application.query.account;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.infrastructure.repository.account.AccountSummaryProjection;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class AccountSummaryRepresentation {

    private final @NonNull String id;
    private final @NonNull String username;
    private final @NonNull String displayedUsername;

    public static AccountSummaryRepresentation fromData(
        @NonNull AccountSummaryProjection data
    ) {
        return AccountSummaryRepresentation.builder()
            .id(data.getId())
            .username(data.getUsername())
            .displayedUsername(data.getDisplayedUsername())
            .build();
    }
}
