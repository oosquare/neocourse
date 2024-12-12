package io.github.oosquare.neocourse.application.command.user;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.utility.test.InitializeTeacherTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@Getter
@SpringBootTest
@Transactional
public class ChangePasswordCommandIntegrationTest implements InitializeTeacherTestSupport {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserCommandService userCommandService;

    @Override
    public EncodedPassword createTestEncodedPassword() {
        return this.toEncodedPassword("password");
    }

    private EncodedPassword toEncodedPassword(String rawPassword) {
        return EncodedPassword.of(this.passwordEncoder.encode(rawPassword));
    }

    @Test
    public void changePasswordSucceeds() {
        var encodedNewPassword = this.toEncodedPassword("new-password");
        var command = ChangePasswordCommand.builder()
            .rawOldPassword("password")
            .encodedNewPassword(encodedNewPassword)
            .build();
        var account = this.createTestTeacherAccount();

        this.userCommandService.changePassword(command, account);

        var res = this.accountRepository.findOrThrow(account.getId());
        assertEquals(encodedNewPassword, res.getEncodedPassword());
    }
}
