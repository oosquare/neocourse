package io.github.oosquare.neocourse.domain.teacher.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.utility.exception.FieldDuplicationException;
import io.github.oosquare.neocourse.utility.id.IdGenerator;

@Service
@AllArgsConstructor
public class TeacherFactory {

    private final @NonNull IdGenerator idGenerator;
    private final @NonNull TeacherRepository teacherRepository;

    public Teacher createTeacher(
        @NonNull Username username,
        @NonNull DisplayedUsername displayedUsername
    ) {
        this.checkUsernameNotDuplicated(username);
        return new Teacher(this.idGenerator.generate(), username, displayedUsername);
    }

    private void checkUsernameNotDuplicated(Username username) {
        this.teacherRepository.findByUsername(username).ifPresent(teacher -> {
            throw FieldDuplicationException.builder()
                .message("Username is duplicated and conflicted with another teacher's")
                .userMessage("Username is duplicated since a teacher with the same username already exists")
                .context("username", username)
                .context("teacher.id", teacher.getId())
                .build();
        });
    }
}
