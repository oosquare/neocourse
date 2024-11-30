package io.github.oosquare.neocourse.infrastructure.repository.account;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountRole;
import io.github.oosquare.neocourse.domain.account.model.AccountRoleKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.annotation.InfrastructureTestTag;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
    AccountConcreteRepository.class,
    AccountMapper.class,
    AccountConverter.class,
    AccountKindConverter.class,
})
@InfrastructureTestTag
class AccountConcreteRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;
    private Account testAccount;

    @BeforeEach
    public void setUp() {
        this.testAccount = Account.builder()
            .id(Id.of("account0"))
            .username(Username.of("student01"))
            .displayedUsername(DisplayedUsername.of("Test Student"))
            .encodedPassword(EncodedPassword.of("encodedPassword123"))
            .role(AccountRoleKind.STUDENT, AccountRole.of(AccountRoleKind.STUDENT, Id.of("student01")))
            .build();
        this.accountRepository.save(this.testAccount);
    }

    @AfterEach
    public void tearDown() {
        this.accountRepository.remove(this.testAccount);
    }

    @Test
    public void findByUsernameWhenDataExists() {
        var account = this.accountRepository.findByUsername(this.testAccount.getUsername()).orElseThrow();
        assertEquals(account.getId(), this.testAccount.getId());
    }

    @Test
    public void findByUsernameWhenDataDoesNotExist() {
        var res = this.accountRepository.findByUsername(Username.of("nonexistent"));
        assertTrue(res.isEmpty());
    }

    @Test
    public void findWhenDataExists() {
        var account = this.accountRepository.find(this.testAccount.getId()).orElseThrow();
        assertEquals(this.testAccount.getId(), account.getId());
    }

    @Test
    public void findWhenDataDoesNotExist() {
        var res = this.accountRepository.find(Id.of("nonexistent"));
        assertTrue(res.isEmpty());
    }

    @Test
    public void save() {
        var anotherAccount = Account.builder()
            .id(Id.of("account1"))
            .username(Username.of("teacher0"))
            .displayedUsername(DisplayedUsername.of("Test Teacher"))
            .encodedPassword(EncodedPassword.of("encodedPassword456"))
            .role(AccountRoleKind.TEACHER, AccountRole.of(AccountRoleKind.TEACHER, Id.of("teacher0")))
            .build();

        this.accountRepository.save(anotherAccount);
        var account = this.accountRepository.find(anotherAccount.getId()).orElseThrow();
        assertEquals(anotherAccount.getId(), account.getId());
        assertEquals(anotherAccount.getUsername(), account.getUsername());
        assertEquals(anotherAccount.getDisplayedUsername(), account.getDisplayedUsername());
        assertEquals(anotherAccount.getRoleKinds(), account.getRoleKinds());
    }

    @Test
    public void remove() {
        this.accountRepository.remove(this.testAccount);
        var res = this.accountRepository.find(this.testAccount.getId());
        assertTrue(res.isEmpty());
    }
}
