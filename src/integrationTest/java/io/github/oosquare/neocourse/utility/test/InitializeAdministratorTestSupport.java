package io.github.oosquare.neocourse.utility.test;

import org.junit.jupiter.api.BeforeEach;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.admin.model.Administrator;
import io.github.oosquare.neocourse.domain.admin.service.AdministratorRepository;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.id.Id;

public interface InitializeAdministratorTestSupport {

    AdministratorRepository getAdministratorRepository();

    AccountRepository getAccountRepository();

    @BeforeEach
    default void setUpDefaultAdministrator() {
        this.getAdministratorRepository().save(this.createTestAdministrator());
        this.getAccountRepository().save(this.createTestAdministratorAccount());
    }

    default Administrator createTestAdministrator() {
        return new Administrator(
            Id.of("test-administrator"),
            Username.of("test-administrator"),
            DisplayedUsername.of("Test Administrator")
        );
    }

    default Account createTestAdministratorAccount() {
        return new Account(
            Id.of("test-administrator-account"),
            AccountKind.ADMINISTRATOR,
            Username.of("test-administrator"),
            DisplayedUsername.of("Test Administrator"),
            EncodedPassword.of("password"),
            Id.of("test-administrator")
        );
    }
}
