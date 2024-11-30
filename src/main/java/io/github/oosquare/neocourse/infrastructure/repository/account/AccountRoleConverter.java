package io.github.oosquare.neocourse.infrastructure.repository.account;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import io.github.oosquare.neocourse.domain.account.model.AccountRole;
import io.github.oosquare.neocourse.infrastructure.repository.DataConverter;
import io.github.oosquare.neocourse.utility.id.Id;

@Component
@AllArgsConstructor
public class AccountRoleConverter implements DataConverter<AccountRole, AccountRoleData> {

    private final @NonNull AccountRoleKindConverter accountRoleKindConverter;

    @Override
    public AccountRole convertToDomain(@NonNull AccountRoleData data) {
        return AccountRole.of(
            this.accountRoleKindConverter.convertToDomain(data.getRoleKind()),
            Id.of(data.getUserDataId())
        );
    }

    @Override
    public AccountRoleData convertToData(@NonNull AccountRole entity) {
        return AccountRoleData.builder()
            .roleKind(this.accountRoleKindConverter.convertToData(entity.getKind()))
            .userDataId(entity.getUserData().getValue())
            .build();
    }
}
