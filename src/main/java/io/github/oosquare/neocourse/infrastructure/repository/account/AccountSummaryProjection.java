package io.github.oosquare.neocourse.infrastructure.repository.account;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountSummaryProjection {

    private String id;
    private String username;
    private String displayedUsername;
}
