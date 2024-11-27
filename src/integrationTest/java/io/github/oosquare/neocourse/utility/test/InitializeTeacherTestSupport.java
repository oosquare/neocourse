package io.github.oosquare.neocourse.utility.test;

import org.junit.jupiter.api.BeforeEach;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.utility.id.Id;

public interface InitializeTeacherTestSupport {

    TeacherRepository getTeacherRepository();

    AccountRepository getAccountRepository();

    @BeforeEach
    default void setUpDefaultTeacher() {
        this.getTeacherRepository().save(this.createTestTeacher());
        this.getAccountRepository().save(this.createTestTeacherAccount());
    }

    default Teacher createTestTeacher() {
        return new Teacher(
            Id.of("test-teacher"),
            Username.of("test-teacher"),
            DisplayedUsername.of("Test Teacher")
        );
    }

    default Account createTestTeacherAccount() {
        return new Account(
            Id.of("test-teacher-account"),
            AccountKind.TEACHER,
            Username.of("test-teacher"),
            DisplayedUsername.of("Test Teacher"),
            EncodedPassword.of("password"),
            Id.of("test-teacher")
        );
    }
}
