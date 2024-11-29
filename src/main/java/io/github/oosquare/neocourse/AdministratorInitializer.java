package io.github.oosquare.neocourse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.github.oosquare.neocourse.application.command.user.BootstrapAdministratorCommand;
import io.github.oosquare.neocourse.application.command.user.UserCommandService;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.exception.ValueValidationException;

@Component
@Profile({"development", "production"})
@AllArgsConstructor
@Slf4j
public class AdministratorInitializer implements CommandLineRunner {

    @Configuration
    @ConfigurationProperties("neocourse.administrator")
    @Data
    public static class AdministratorConfig {

        private String username;
        private String displayedUsername;
        private String password;
    }

    private final @NonNull PasswordEncoder passwordEncoder;
    private final @NonNull UserCommandService userCommandService;
    private final @NonNull AdministratorConfig administratorConfig;

    @Override
    public void run(String... args) {
        if (this.administratorConfig.getUsername().isEmpty()) {
            log.info("Skipped default administrator initialization");
            return;
        }

        this.validateConfig();
        this.initializeAdministrator();
    }

    private void validateConfig() {
        ValueValidationException.validator()
            .ensure(!this.administratorConfig.getDisplayedUsername().isEmpty())
            .message("Requires key 'neocourse.administrator.displayed-username' for the default administrator")
            .done();

        ValueValidationException.validator()
            .ensure(!this.administratorConfig.getPassword().isEmpty())
            .message("Requires key 'neocourse.administrator.password' for the default administrator")
            .done();
    }

    private void initializeAdministrator() {
        var password = this.passwordEncoder.encode(this.administratorConfig.getPassword());
        var command = BootstrapAdministratorCommand.builder()
            .username(Username.of(this.administratorConfig.getUsername()))
            .displayedUsername(DisplayedUsername.of(this.administratorConfig.getDisplayedUsername()))
            .encodedPassword(EncodedPassword.of(password))
            .build();
        this.userCommandService.bootstrapAdministrator(command);
    }
}
