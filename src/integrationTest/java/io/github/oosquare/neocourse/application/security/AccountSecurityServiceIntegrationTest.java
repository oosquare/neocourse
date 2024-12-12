package io.github.oosquare.neocourse.application.security;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.utility.exception.UserAuthorizationException;
import io.github.oosquare.neocourse.utility.test.InitializeTeacherTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@Getter
@SpringBootTest
@Transactional
public class AccountSecurityServiceIntegrationTest implements InitializeTeacherTestSupport {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountSecurityService accountSecurityService;

    @Override
    public EncodedPassword createTestEncodedPassword() {
        return EncodedPassword.of(this.passwordEncoder.encode("password"));
    }

    @Test
    public void checkPasswordCorrectSucceeds() {
        var account = this.createTestTeacherAccount();
        var password = "password";

        this.accountSecurityService.checkPasswordCorrect(account, password);
    }

    @Test
    public void checkPasswordCorrectThrowsWhenPasswordIsWrong() {
        var account = this.createTestTeacherAccount();
        var password = "wrong-password";

        assertThrows(UserAuthorizationException.class, () -> {
            this.accountSecurityService.checkPasswordCorrect(account, password);
        });
    }
}
