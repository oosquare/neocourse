package io.github.oosquare.neocourse.domain.common.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
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

    public void checkIsUser(@NonNull Account account, @NonNull AccountKind kind) {
        if (account.getKind() != kind) {
            throw UserAuthorizationException.builder()
                .message("Account is not of a given kind")
                .userMessage("Require a(n) %s account to proceed")
                .context("account.id", account.getId())
                .context("account.username", account.getUsername())
                .context("account.kind", account.getKind())
                .context("kind", kind)
                .build();
        }
    }

    public Student getStudentUser(@NonNull Account account) {
        this.checkIsUser(account, AccountKind.STUDENT);
        return this.studentRepository.findByUsername(account.getUsername())
            .orElseThrow();
    }

    public Teacher getTeacherUser(@NonNull Account account) {
        this.checkIsUser(account, AccountKind.TEACHER);
        return this.teacherRepository.findByUsername(account.getUsername())
            .orElseThrow();
    }

    public Administrator getAdministratorUser(@NonNull Account account) {
        this.checkIsUser(account, AccountKind.ADMINISTRATOR);
        return this.administratorRepository.findByUsername(account.getUsername())
            .orElseThrow();
    }
}
