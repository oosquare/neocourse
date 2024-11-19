package io.github.oosquare.neocourse.domain.admin.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.admin.model.Administrator;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.exception.FieldDuplicationException;
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

    private void checkUsernameNotDuplicated(Username username) {
        this.administratorRepository.findByUsername(username).ifPresent(administrator -> {
            throw FieldDuplicationException.builder()
                .message("Username is duplicated and conflicted with another administrator's")
                .userMessage("Username is duplicated since an administrator with the same username already exists")
                .context("username", username)
                .context("administrator.id", administrator.getId())
                .build();
        });
    }
}
