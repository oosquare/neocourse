package io.github.oosquare.neocourse.application.command.user;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountRoleKind;
import io.github.oosquare.neocourse.domain.account.service.AccountFactory;
import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.account.service.AccountService;
import io.github.oosquare.neocourse.domain.admin.service.AdministratorFactory;
import io.github.oosquare.neocourse.domain.admin.service.AdministratorRepository;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.domain.student.service.StudentFactory;
import io.github.oosquare.neocourse.domain.student.service.StudentRepository;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherFactory;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.domain.transcript.service.TranscriptRepository;

@Service
@AllArgsConstructor
@Slf4j
public class UserCommandService {

    private final @NonNull AccountFactory accountFactory;
    private final @NonNull AccountRepository accountRepository;
    private final @NonNull AccountService accountService;
    private final @NonNull StudentFactory studentFactory;
    private final @NonNull StudentRepository studentRepository;
    private final @NonNull TeacherFactory teacherFactory;
    private final @NonNull TeacherRepository teacherRepository;
    private final @NonNull AdministratorFactory administratorFactory;
    private final @NonNull AdministratorRepository administratorRepository;
    private final @NonNull TranscriptRepository transcriptRepository;
    private final @NonNull PlanRepository planRepository;

    @Transactional
    public void signUpStudent(@NonNull SignUpStudentCommand command) {
        log.info("Anonymous requests signUpStudent with {}", command);

        var username = command.getUsername();
        var displayedUsername = command.getDisplayedUsername();
        var planId = command.getPlanId();
        var encodedPassword = command.getEncodedPassword();

        var plan = this.planRepository.findOrThrow(planId);

        var res = this.studentFactory.createStudentAndTranscript(username, displayedUsername, plan);
        var student = res.getStudent();
        var transcript = res.getTranscript();
        var account = this.accountFactory.createAccount(AccountRoleKind.STUDENT, student, encodedPassword);

        this.studentRepository.save(student);
        this.transcriptRepository.save(transcript);
        this.accountRepository.save(account);

        log.info(
            "Created Student[id={}, username={}, plan={}], Account[id={}, kind={}] and Transcript[id={}]",
            student.getId(),
            student.getUsername(),
            student.getPlan(),
            account.getId(),
            account.roleKindsToString(),
            transcript.getId()
        );
    }

    @Transactional
    public void signUpTeacher(@NonNull SignUpTeacherCommand command) {
        log.info("Anonymous requests signUpTeacher with {}", command);

        var username = command.getUsername();
        var displayedUsername = command.getDisplayedUsername();
        var encodedPassword = command.getEncodedPassword();

        var teacher = this.teacherFactory.createTeacher(username, displayedUsername);
        var account = this.accountFactory.createAccount(AccountRoleKind.TEACHER, teacher, encodedPassword);

        this.teacherRepository.save(teacher);
        this.accountRepository.save(account);

        log.info(
            "Created Teacher[id={}, username={}] and Account[id={}, kind={}]",
            teacher.getId(),
            teacher.getUsername(),
            account.getId(),
            account.roleKindsToString()
        );
    }

    @Transactional
    public void bootstrapAdministrator(@NonNull BootstrapAdministratorCommand command) {
        log.warn("Initialize default administrator in signUpAdministrator with {}", command);

        var username = command.getUsername();
        var displayedUsername = command.getDisplayedUsername();
        var encodedPassword = command.getEncodedPassword();

        var administrator = this.administratorFactory.createAdministrator(username, displayedUsername);
        var account = this.accountFactory.createAccount(AccountRoleKind.ADMINISTRATOR, administrator, encodedPassword);

        this.administratorRepository.save(administrator);
        this.accountRepository.save(account);

        log.warn(
            "Created Administrator[id={}, username={}] and Account[id={}, kind={}]",
            administrator.getId(),
            administrator.getUsername(),
            account.getId(),
            account.roleKindsToString()
        );
    }

    @Transactional
    public void upgradeToAdministrator(
        @NonNull UpgradeToAdministratorCommand command,
        @NonNull Account account
    ) {
        log.warn("{} requests upgradeToAdministrator with {}", account.toLoggingString(), command);

        var accountId = command.getAccountId();

        this.accountService.checkHasRole(account, AccountRoleKind.ADMINISTRATOR);
        var accountToUpgrade = this.accountRepository.findOrThrow(accountId);
        this.accountService.checkHasRole(accountToUpgrade, AccountRoleKind.TEACHER);

        var administrator = this.administratorFactory.createAdministrator(
            accountToUpgrade.getUsername(),
            accountToUpgrade.getDisplayedUsername()
        );
        accountToUpgrade.addRole(AccountRoleKind.ADMINISTRATOR, administrator.getId());

        this.accountRepository.save(accountToUpgrade);
        this.administratorRepository.save(administrator);

        log.warn(
            "Upgraded Account[id={}, username={}, kind={}] by {}",
            accountToUpgrade.getId(),
            accountToUpgrade.getUsername(),
            accountToUpgrade.roleKindsToString(),
            account.toLoggingString()
        );
    }
}
