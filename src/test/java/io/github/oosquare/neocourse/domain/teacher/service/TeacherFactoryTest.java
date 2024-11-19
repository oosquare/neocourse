package io.github.oosquare.neocourse.domain.teacher.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.utility.exception.FieldDuplicationException;
import io.github.oosquare.neocourse.utility.id.Id;
import io.github.oosquare.neocourse.utility.id.IdGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherFactoryTest {

    private @Mock IdGenerator idGenerator;
    private @Mock TeacherRepository teacherRepository;
    private @InjectMocks TeacherFactory teacherFactory;

    @Test
    void createTeacherSucceeds() {
        when(this.idGenerator.generate())
            .thenReturn(Id.of("0"));
        when(this.teacherRepository.findByUsername(any()))
            .thenReturn(Optional.empty());

        var course = this.teacherFactory.createTeacher(
            Username.of("test-teacher"),
            DisplayedUsername.of("test teacher")
        );
        assertEquals(Id.of("0"), course.getId());
        assertEquals(Username.of("test-teacher"), course.getUsername());
        assertEquals(DisplayedUsername.of("test teacher"), course.getDisplayedUsername());
    }

    @Test
    void createTeacherThrowsWhenUsernameIsDuplicated() {
        when(this.teacherRepository.findByUsername(Username.of("test-teacher")))
            .thenReturn(Optional.of(new Teacher(
                Id.of("0"),
                Username.of("test-teacher"),
                DisplayedUsername.of("test teacher")
            )));

        assertThrows(FieldDuplicationException.class, () -> {
            this.teacherFactory.createTeacher(
                Username.of("test-teacher"),
                DisplayedUsername.of("test teacher")
            );
        });
    }
}