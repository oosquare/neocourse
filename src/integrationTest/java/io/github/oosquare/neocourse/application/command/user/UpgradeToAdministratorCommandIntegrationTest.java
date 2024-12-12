package io.github.oosquare.neocourse.application.command.user;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.AccountRoleKind;
import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.admin.service.AdministratorRepository;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.utility.test.InitializeAdministratorTestSupport;
import io.github.oosquare.neocourse.utility.test.InitializeTeacherTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@Getter
@SpringBootTest
@Transactional
public class UpgradeToAdministratorCommandIntegrationTest
    implements InitializeAdministratorTestSupport, InitializeTeacherTestSupport {

    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserCommandService userCommandService;

    @Test
    public void upgradeToAdministratorSucceeds() {
        var teacherAccount = this.createTestTeacherAccount();

        var command = UpgradeToAdministratorCommand.builder()
            .accountId(teacherAccount.getId())
            .build();
        var account = this.createTestAdministratorAccount();

        this.userCommandService.upgradeToAdministrator(command, account);

        var resAccount = this.accountRepository.findOrThrow(teacherAccount.getId());
        assertTrue(resAccount.hasRole(AccountRoleKind.ADMINISTRATOR));
        var resAdministratorId = resAccount.getUserData(AccountRoleKind.ADMINISTRATOR);
        var resAdministrator = this.administratorRepository.findOrThrow(resAdministratorId);
        assertEquals(resAccount.getUsername(), resAdministrator.getUsername());
    }
}
