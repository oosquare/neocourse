package io.github.oosquare.neocourse.infrastructure.repository.account;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import io.github.oosquare.neocourse.domain.account.model.AccountRoleKind;
import io.github.oosquare.neocourse.infrastructure.repository.DataConverter;

@Component
public class AccountRoleKindConverter implements DataConverter<AccountRoleKind, AccountRoleKindData> {

    @Override
    public AccountRoleKind convertToDomain(@NonNull AccountRoleKindData data) {
        return switch (data) {
            case STUDENT -> AccountRoleKind.STUDENT;
            case TEACHER -> AccountRoleKind.TEACHER;
            case ADMINISTRATOR -> AccountRoleKind.ADMINISTRATOR;
        };
    }

    @Override
    public AccountRoleKindData convertToData(@NonNull AccountRoleKind entity) {
        return switch (entity) {
            case STUDENT -> AccountRoleKindData.STUDENT;
            case TEACHER -> AccountRoleKindData.TEACHER;
            case ADMINISTRATOR -> AccountRoleKindData.ADMINISTRATOR;
        };
    }
}
