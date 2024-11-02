package io.github.oosquare.neocourse.domain.admin.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.admin.exception.CreateAdministratorException;
import io.github.oosquare.neocourse.domain.admin.model.Administrator;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.id.IdGenerator;

@Service
@AllArgsConstructor
public class AdministratorFactory {

    private final @NonNull IdGenerator idGenerator;
    private final @NonNull AdministratorRepository administratorRepository;

    public Administrator createAdministrator(
        @NonNull Username username,
        @NonNull DisplayedUsername displayedUsername
    ) {
        this.checkUsernameNotDuplicated(username);
        return new Administrator(this.idGenerator.generate(), username, displayedUsername);
    }

    private void checkUsernameNotDuplicated(@NonNull Username username) {
        this.administratorRepository.findByUsername(username)
            .ifPresent(administrator -> {
                throw new CreateAdministratorException(
                    String.format(
                        "Administrator[id=%s, username=%s] already exists",
                        administrator.getId(),
                        administrator.getUsername()
                    )
                );
            });
    }
}
