package io.github.oosquare.neocourse.infrastructure.repository.account;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.infrastructure.repository.DataConverter;

@Component
public class AccountKindConverter implements DataConverter<AccountKind, AccountKindData> {

    @Override
    public AccountKind convertToDomain(@NonNull AccountKindData data) {
        return switch (data) {
            case STUDENT -> AccountKind.STUDENT;
            case TEACHER -> AccountKind.TEACHER;
            case ADMINISTRATOR -> AccountKind.ADMINISTRATOR;
        };
    }

    @Override
    public AccountKindData convertToData(@NonNull AccountKind entity) {
        return switch (entity) {
            case STUDENT -> AccountKindData.STUDENT;
            case TEACHER -> AccountKindData.TEACHER;
            case ADMINISTRATOR -> AccountKindData.ADMINISTRATOR;
        };
    }
}
