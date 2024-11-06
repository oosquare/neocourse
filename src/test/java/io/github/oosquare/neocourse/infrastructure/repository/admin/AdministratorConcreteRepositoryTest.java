package io.github.oosquare.neocourse.infrastructure.repository.admin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import io.github.oosquare.neocourse.domain.admin.model.Administrator;
import io.github.oosquare.neocourse.domain.admin.service.AdministratorRepository;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.annotation.InfrastructureTestTag;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
    AdministratorConcreteRepository.class,
    AdministratorMapper.class,
    AdministratorConverter.class,
})
@InfrastructureTestTag
class AdministratorConcreteRepositoryTest {

    @Autowired
    private AdministratorRepository administratorRepository;
    private Administrator testAdministrator;

    @BeforeEach
    public void setUp() {
        this.testAdministrator = Administrator.createInternally(
            Id.of("admin0"),
            Username.of("admin"),
            DisplayedUsername.of("Administrator")
        );
        this.administratorRepository.save(this.testAdministrator);
    }

    @AfterEach
    public void tearDown() {
        this.administratorRepository.remove(this.testAdministrator);
    }

    @Test
    public void findByUsernameWhenDataExists() {
        var res = this.administratorRepository.findByUsername(this.testAdministrator.getUsername());
        assertTrue(res.isPresent());
        var administrator = res.get();
        assertEquals(administrator.getId(), this.testAdministrator.getId());
        assertEquals(administrator.getUsername(), this.testAdministrator.getUsername());
        assertEquals(administrator.getDisplayedUsername(), this.testAdministrator.getDisplayedUsername());
    }

    @Test
    public void findByUsernameWhenDataDoesNotExist() {
        var res = this.administratorRepository.findByUsername(Username.of("nonexistent"));
        assertTrue(res.isEmpty());
    }

    @Test
    public void findWhenDataExists() {
        var res = this.administratorRepository.find(this.testAdministrator.getId());
        assertTrue(res.isPresent());
        var administrator = res.get();
        assertEquals(this.testAdministrator.getId(), administrator.getId());
        assertEquals(this.testAdministrator.getUsername(), administrator.getUsername());
        assertEquals(this.testAdministrator.getDisplayedUsername(), administrator.getDisplayedUsername());
    }

    @Test
    public void findWhenDataDoesNotExist() {
        var res = this.administratorRepository.find(Id.of("nonexistent"));
        assertTrue(res.isEmpty());
    }

    @Test
    public void save() {
        var anotherAdministrator = Administrator.createInternally(
            Id.of("admin1"),
            Username.of("another-admin"),
            DisplayedUsername.of("Another Administrator")
        );
        this.administratorRepository.save(anotherAdministrator);
        var res = this.administratorRepository.find(anotherAdministrator.getId());
        assertTrue(res.isPresent());
        var administrator = res.get();
        assertEquals(anotherAdministrator.getId(), administrator.getId());
        assertEquals(anotherAdministrator.getUsername(), administrator.getUsername());
        assertEquals(anotherAdministrator.getDisplayedUsername(), administrator.getDisplayedUsername());
    }

    @Test
    public void remove() {
        this.administratorRepository.remove(this.testAdministrator);
        var res = this.administratorRepository.find(this.testAdministrator.getId());
        assertTrue(res.isEmpty());
    }
}