package io.github.oosquare.neocourse.infrastructure.repository.account;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.infrastructure.repository.DataConverter;
import io.github.oosquare.neocourse.utility.id.Id;

@Component
@AllArgsConstructor
public class AccountConverter implements DataConverter<Account, AccountData> {

    private final @NonNull AccountKindConverter accountKindConverter;

    @Override
    public Account convertToDomain(@NonNull AccountData data) {
        return Account.builder()
            .id(Id.of(data.getId()))
            .kind(this.accountKindConverter.convertToDomain(data.getKind()))
            .username(Username.of(data.getUsername()))
            .displayedUsername(DisplayedUsername.of(data.getDisplayedUsername()))
            .encodedPassword(EncodedPassword.of(data.getEncodedPassword()))
            .user(Id.of(data.getUserId()))
            .build();
    }

    @Override
    public AccountData convertToData(@NonNull Account entity) {
        return AccountData.builder()
            .id(entity.getId().getValue())
            .kind(this.accountKindConverter.convertToData(entity.getKind()))
            .username(entity.getUsername().getValue())
            .displayedUsername(entity.getDisplayedUsername().getValue())
            .encodedPassword(entity.getEncodedPassword().getValue())
            .userId(entity.getUser().getValue())
            .build();
    }
}
