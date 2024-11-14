package io.github.oosquare.neocourse.domain.account.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.oosquare.neocourse.domain.account.exception.AccountException;
import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.id.Id;
import io.github.oosquare.neocourse.utility.id.IdGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountFactoryTest {

    private @Mock IdGenerator idGenerator;
    private @Mock AccountRepository accountRepository;
    private @InjectMocks AccountFactory accountFactory;

    @Test
    public void createAccountSucceeds() {
        when(this.idGenerator.generate())
            .thenReturn(Id.of("account0"));
        when(this.accountRepository.findByUsername(any()))
            .thenReturn(Optional.empty());

        var account = this.accountFactory.createAccount(
            AccountKind.STUDENT,
            Username.of("student0"),
            DisplayedUsername.of("test student"),
            EncodedPassword.of("test password"),
            Id.of("student0")
        );

        assertEquals(Id.of("account0"), account.getId());
    }

    @Test
    public void createAccountThrowsWhenUsernameIsDuplicated() {
        when(this.accountRepository.findByUsername(Username.of("student0")))
            .thenReturn(Optional.of(Account.builder()
                .id(Id.of("account0"))
                .kind(AccountKind.STUDENT)
                .username(Username.of("student0"))
                .displayedUsername(DisplayedUsername.of("test student"))
                .encodedPassword(EncodedPassword.of("test password"))
                .user(Id.of("student0"))
                .build()));

        assertThrows(AccountException.class, () -> {
            this.accountFactory.createAccount(
                AccountKind.STUDENT,
                Username.of("student0"),
                DisplayedUsername.of("test student"),
                EncodedPassword.of("test password"),
                Id.of("student0")
            );
        });
    }
}