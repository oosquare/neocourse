package io.github.oosquare.neocourse.domain.account.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountRoleKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.User;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.exception.FieldDuplicationException;
import io.github.oosquare.neocourse.utility.id.IdGenerator;

@Service
@AllArgsConstructor
public class AccountFactory {

    private final @NonNull IdGenerator idGenerator;
    private final @NonNull AccountRepository accountRepository;

    public Account createAccount(
        @NonNull AccountRoleKind kind,
        @NonNull User user,
        @NonNull EncodedPassword encodedPassword
    ) {
        this.checkUsernameNotDuplicated(user.getUsername());
        var id = this.idGenerator.generate();
        return new Account(id, kind, user, encodedPassword);
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
