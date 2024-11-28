package io.github.oosquare.neocourse.application.command.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.admin.service.AdministratorRepository;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class BootstrapAdministratorCommandIntegrationTest {

    @SpyBean
    private AdministratorRepository administratorRepository;
    @Autowired
    private UserCommandService userCommandService;

    @BeforeEach
    public void resetSpy() {
        reset(this.administratorRepository);
    }

    @Test
    public void bootstrapAdministratorSucceeds() {
        var command = BootstrapAdministratorCommand.builder()
            .username(Username.of("test-teacher"))
            .displayedUsername(DisplayedUsername.of("Test Teacher"))
            .encodedPassword(EncodedPassword.of("encoded-password"))
            .build();

        this.userCommandService.bootstrapAdministrator(command);

        verify(this.administratorRepository).save(assertArg(administrator -> {
            var res = this.administratorRepository.findOrThrow(administrator.getId());
            assertEquals(Username.of("test-teacher"), res.getUsername());
            assertEquals(DisplayedUsername.of("Test Teacher"), res.getDisplayedUsername());
        }));
    }
}
