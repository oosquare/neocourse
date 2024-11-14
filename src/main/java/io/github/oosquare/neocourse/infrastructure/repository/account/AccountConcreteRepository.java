package io.github.oosquare.neocourse.infrastructure.repository.account;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.id.Id;

@Repository
@AllArgsConstructor
public class AccountConcreteRepository implements AccountRepository {

    private final @NonNull AccountMapper accountMapper;
    private final @NonNull AccountConverter accountConverter;

    @Override
    public Optional<Account> findByUsername(@NonNull Username username) {
        return this.accountMapper.findByUsername(username.getValue())
            .map(this.accountConverter::convertToDomain);
    }

    @Override
    public Optional<Account> find(@NonNull Id id) {
        return this.accountMapper.find(id.getValue())
            .map(this.accountConverter::convertToDomain);
    }

    @Override
    public void save(@NonNull Account entity) {
        var data = this.accountConverter.convertToData(entity);
        this.accountMapper.save(data);
    }

    @Override
    public void remove(@NonNull Account entity) {
        var data = this.accountConverter.convertToData(entity);
        this.accountMapper.remove(data);
    }
}
