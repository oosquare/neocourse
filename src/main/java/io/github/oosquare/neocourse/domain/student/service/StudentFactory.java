package io.github.oosquare.neocourse.domain.student.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.transcript.model.Transcript;
import io.github.oosquare.neocourse.utility.exception.FieldDuplicationException;
import io.github.oosquare.neocourse.utility.id.IdGenerator;

@Service
@AllArgsConstructor
public class StudentFactory {

    private final @NonNull IdGenerator idGenerator;
    private final @NonNull StudentRepository studentRepository;

    public CreateStudentResult createStudentAndTranscript(
        @NonNull Username username,
        @NonNull DisplayedUsername displayedUsername,
        @NonNull Plan plan
    ) {
        this.checkUsernameNotDuplicated(username);
        var transcript = new Transcript(this.idGenerator.generate(), plan);
        var student = new Student(
            this.idGenerator.generate(),
            username,
            displayedUsername,
            plan,
            transcript
        );
        return new CreateStudentResult(student, transcript);
    }

    private void checkUsernameNotDuplicated(Username username) {
        this.studentRepository.findByUsername(username).ifPresent(student -> {
            throw FieldDuplicationException.builder()
                .message("Username is duplicated and conflicted with another student's")
                .userMessage("Username is duplicated since a student with the same username already exists")
                .context("username", username)
                .context("student.id", student.getId())
                .build();
        });
    }
}
