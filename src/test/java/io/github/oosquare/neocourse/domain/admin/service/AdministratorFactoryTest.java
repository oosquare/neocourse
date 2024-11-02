package io.github.oosquare.neocourse.domain.admin.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.oosquare.neocourse.domain.admin.exception.CreateAdministratorException;
import io.github.oosquare.neocourse.domain.admin.model.Administrator;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.id.Id;
import io.github.oosquare.neocourse.utility.id.IdGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdministratorFactoryTest {

    private @Mock IdGenerator idGenerator;
    private @Mock AdministratorRepository administratorRepository;
    private @InjectMocks AdministratorFactory administratorFactory;

    @Test
    void createAdministratorSucceeds() {
        when(this.idGenerator.generate())
            .thenReturn(Id.of("0"));
        when(this.administratorRepository.findByUsername(any()))
            .thenReturn(Optional.empty());

        var course = this.administratorFactory.createAdministrator(
            Username.of("test-admin"),
            DisplayedUsername.of("test admin")
        );
        assertEquals(Id.of("0"), course.getId());
        assertEquals(Username.of("test-admin"), course.getUsername());
        assertEquals(DisplayedUsername.of("test admin"), course.getDisplayedUsername());
    }

    @Test
    void createAdministratorThrowsWhenUsernameIsDuplicated() {
        when(this.administratorRepository.findByUsername(Username.of("test-admin")))
            .thenReturn(Optional.of(new Administrator(
                Id.of("0"),
                Username.of("test-admin"),
                DisplayedUsername.of("test admin")
            )));

        assertThrows(CreateAdministratorException.class, () -> {
            this.administratorFactory.createAdministrator(
                Username.of("test-admin"),
                DisplayedUsername.of("test admin")
            );
        });
    }
}