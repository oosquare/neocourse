package io.github.oosquare.neocourse.domain.common.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountRoleKind;
import io.github.oosquare.neocourse.domain.admin.model.Administrator;
import io.github.oosquare.neocourse.domain.admin.service.AdministratorRepository;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.student.service.StudentRepository;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.utility.exception.UserAuthorizationException;

@Service
@AllArgsConstructor
public class UserService {

    private final @NonNull StudentRepository studentRepository;
    private final @NonNull TeacherRepository teacherRepository;
    private final @NonNull AdministratorRepository administratorRepository;

    public void checkHasRole(@NonNull Account account, @NonNull AccountRoleKind kind) {
        if (!account.hasRole(kind)) {
            throw UserAuthorizationException.builder()
                .message("Account is not of a given kind")
                .userMessage("Require a(n) %s account to proceed".formatted(kind))
                .context("account.id", account.getId())
                .context("account.username", account.getUsername())
                .context("account.kind", account.roleKindsToString())
                .context("kind", kind)
                .build();
        }
    }

    public Student getStudentUser(@NonNull Account account) {
        this.checkHasRole(account, AccountRoleKind.STUDENT);
        return this.studentRepository.findByUsername(account.getUsername())
            .orElseThrow();
    }

    public Teacher getTeacherUser(@NonNull Account account) {
        this.checkHasRole(account, AccountRoleKind.TEACHER);
        return this.teacherRepository.findByUsername(account.getUsername())
            .orElseThrow();
    }

    public Administrator getAdministratorUser(@NonNull Account account) {
        this.checkHasRole(account, AccountRoleKind.ADMINISTRATOR);
        return this.administratorRepository.findByUsername(account.getUsername())
            .orElseThrow();
    }
}
