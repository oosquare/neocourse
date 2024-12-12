package io.github.oosquare.neocourse.infrastructure.repository.account;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.infrastructure.repository.DataMapper;

@Repository
public class AccountMapper extends DataMapper<AccountData> {

    public AccountMapper(@NonNull EntityManager entityManager) {
        super(entityManager, AccountData.class);
    }

    public Optional<AccountData> findByUsername(@NonNull String username) {
        var data = this.getEntityManager()
            .createNamedQuery("AccountData.findByUsername", AccountData.class)
            .setParameter("username", username)
            .setMaxResults(1)
            .getResultList();
        return (data.isEmpty() ? Optional.empty() : Optional.of(data.getFirst()));
    }

    public List<AccountSummaryProjection> findAllReturningSummaryProjection() {
        return this.getEntityManager()
            .createNamedQuery(
                "AccountData.findAllReturningSummaryProjection",
                AccountSummaryProjection.class
            )
            .getResultList();
    }

    public Optional<AccountData> findById(
        @NonNull String accountId
    ) {
        var data = this.getEntityManager()
            .createNamedQuery("AccountData.findById", AccountData.class)
            .setParameter("accountId", accountId)
            .setMaxResults(1)
            .getResultList();
        return (data.isEmpty() ? Optional.empty() : Optional.of(data.getFirst()));
    }
}
