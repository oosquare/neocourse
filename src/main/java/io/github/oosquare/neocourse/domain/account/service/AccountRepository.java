package io.github.oosquare.neocourse.domain.account.service;

import java.util.Optional;

import lombok.NonNull;

import io.github.oosquare.neocourse.domain.EntityRepository;
import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.common.model.Username;

public interface AccountRepository extends EntityRepository<Account> {

    Optional<Account> findByUsername(@NonNull Username username);
}
