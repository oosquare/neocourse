package io.github.oosquare.neocourse.domain.account.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.exception.FieldDuplicationException;
import io.github.oosquare.neocourse.utility.id.Id;
import io.github.oosquare.neocourse.utility.id.IdGenerator;

@Service
@AllArgsConstructor
public class AccountFactory {

    private final @NonNull IdGenerator idGenerator;
    private final @NonNull AccountRepository accountRepository;

    public Account createAccount(
        @NonNull AccountKind kind,
        @NonNull Username username,
        @NonNull DisplayedUsername displayedUsername,
        @NonNull EncodedPassword encodedPassword,
        @NonNull Id user
    ) {
        this.checkUsernameNotDuplicated(username);
        var id = this.idGenerator.generate();
        return new Account(
            id,
            kind,
            username,
            displayedUsername,
            encodedPassword,
            user
        );
    }

    private void checkUsernameNotDuplicated(Username username) {
        this.accountRepository.findByUsername(username).ifPresent(account -> {
            throw FieldDuplicationException.builder()
                .message("Username is duplicated and conflicted with another account's")
                .userMessage("Username is duplicated since an account with the same username already exists")
                .context("username", username)
                .context("account.id", account.getId())
                .build();
        });
    }
}
