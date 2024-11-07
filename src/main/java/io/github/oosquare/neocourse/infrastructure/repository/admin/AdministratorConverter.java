package io.github.oosquare.neocourse.infrastructure.repository.admin;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import io.github.oosquare.neocourse.domain.admin.model.Administrator;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.infrastructure.repository.DataConverter;
import io.github.oosquare.neocourse.utility.id.Id;

@Component
public class AdministratorConverter implements DataConverter<Administrator, AdministratorData> {

    @Override
    public Administrator convertToDomain(@NonNull AdministratorData data) {
        return Administrator.builder()
            .id(Id.of(data.getId()))
            .username(Username.of(data.getUsername()))
            .displayedUsername(DisplayedUsername.of(data.getDisplayedUsername()))
            .build();
    }

    @Override
    public AdministratorData convertToData(@NonNull Administrator entity) {
        return AdministratorData.builder()
            .id(entity.getId().getValue())
            .username(entity.getUsername().getValue())
            .displayedUsername(entity.getDisplayedUsername().getValue())
            .build();
    }
}
