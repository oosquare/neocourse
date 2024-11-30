package io.github.oosquare.neocourse.infrastructure.repository.account;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import io.github.oosquare.neocourse.domain.account.model.AccountRoleKind;
import io.github.oosquare.neocourse.infrastructure.repository.DataConverter;

@Component
public class AccountKindConverter implements DataConverter<AccountRoleKind, AccountKindData> {

    @Override
    public AccountRoleKind convertToDomain(@NonNull AccountKindData data) {
        return switch (data) {
            case STUDENT -> AccountRoleKind.STUDENT;
            case TEACHER -> AccountRoleKind.TEACHER;
            case ADMINISTRATOR -> AccountRoleKind.ADMINISTRATOR;
        };
    }

    @Override
    public AccountKindData convertToData(@NonNull AccountRoleKind entity) {
        return switch (entity) {
            case STUDENT -> AccountKindData.STUDENT;
            case TEACHER -> AccountKindData.TEACHER;
            case ADMINISTRATOR -> AccountKindData.ADMINISTRATOR;
        };
    }
}
