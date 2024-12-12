package io.github.oosquare.neocourse.application.query.account;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountRoleKind;
import io.github.oosquare.neocourse.domain.account.service.AccountService;
import io.github.oosquare.neocourse.infrastructure.repository.account.AccountMapper;
import io.github.oosquare.neocourse.utility.exception.EntityNotFoundException;
import io.github.oosquare.neocourse.utility.id.Id;

@Service
@AllArgsConstructor
@Slf4j
public class AccountQueryService {

    private final @NonNull AccountService accountService;
    private final @NonNull AccountMapper accountMapper;

    @Transactional
    public List<AccountSummaryRepresentation> getAllAccountsInSummaryRepresentation(@NonNull Account account) {
        log.info("{} requests getAllAccountsInSummaryRepresentation", account.toLoggingString());

        this.accountService.checkHasRole(account, AccountRoleKind.ADMINISTRATOR);

        return this.accountMapper.findAllReturningSummaryProjection()
            .stream()
            .map(AccountSummaryRepresentation::fromData)
            .toList();
    }

    @Transactional
    public AccountDetailedRepresentation getAccountByIdInDetailedRepresentation(
        @NonNull Id accountId,
        @NonNull Account account
    ) {
        log.info(
            "{} requests getAccountByIdInDetailedRepresentation with {}",
            account.toLoggingString(),
            accountId
        );

        return this.accountMapper.findById(accountId.getValue())
            .map(AccountDetailedRepresentation::fromData)
            .orElseThrow(() -> EntityNotFoundException.builder()
                .entity(AccountDetailedRepresentation.class)
                .context("accountId", accountId)
                .build());
    }
}
