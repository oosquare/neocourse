package io.github.oosquare.neocourse.infrastructure.repository.account;

import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
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

    private final @NonNull AccountRoleConverter accountRoleConverter;
    private final @NonNull AccountRoleKindConverter accountRoleKindConverter;

    @Override
    public Account convertToDomain(@NonNull AccountData data) {
        var roles = data.getRoles()
            .stream()
            .collect(Collectors.toMap(
                dat -> this.accountRoleKindConverter.convertToDomain(dat.getRoleKind()),
                this.accountRoleConverter::convertToDomain
            ));
        return Account.builder()
            .id(Id.of(data.getId()))
            .username(Username.of(data.getUsername()))
            .displayedUsername(DisplayedUsername.of(data.getDisplayedUsername()))
            .encodedPassword(EncodedPassword.of(data.getEncodedPassword()))
            .roles(ImmutableMap.copyOf(roles))
            .build();
    }

    @Override
    public AccountData convertToData(@NonNull Account entity) {
        var roles = entity.getRoles()
            .values()
            .stream()
            .map(this.accountRoleConverter::convertToData)
            .collect(Collectors.toSet());
        return AccountData.builder()
            .id(entity.getId().getValue())
            .roles(roles)
            .username(entity.getUsername().getValue())
            .displayedUsername(entity.getDisplayedUsername().getValue())
            .encodedPassword(entity.getEncodedPassword().getValue())
            .build();
    }
}
