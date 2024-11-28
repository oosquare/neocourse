package io.github.oosquare.neocourse.utility.test;

import org.junit.jupiter.api.BeforeEach;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.student.service.StudentRepository;
import io.github.oosquare.neocourse.domain.transcript.model.Transcript;
import io.github.oosquare.neocourse.domain.transcript.service.TranscriptRepository;
import io.github.oosquare.neocourse.utility.id.Id;

public interface InitializeStudentTestSupport extends InitializePlanTestSupport {

    StudentRepository getStudentRepository();

    AccountRepository getAccountRepository();

    TranscriptRepository getTranscriptRepository();

    default Student createTestStudent() {
        return new Student(
            Id.of("test-student"),
            Username.of("test-student"),
            DisplayedUsername.of("Test Student"),
            this.createTestPlan(),
            this.createTestTranscript()
        );
    }

    default Account createTestStudentAccount() {
        return new Account(
            Id.of("test-student-account"),
            AccountKind.STUDENT,
            this.createTestStudent(),
            EncodedPassword.of("password")
        );
    }

    default Transcript createTestTranscript() {
        return new Transcript(Id.of("test-transcript"), this.createTestPlan());
    }

    @BeforeEach
    default void setUpDefaultStudents() {
        this.getStudentRepository().save(this.createTestStudent());
        this.getAccountRepository().save(this.createTestStudentAccount());
        this.getTranscriptRepository().save(this.createTestTranscript());
    }
}
