package io.github.oosquare.neocourse.domain.student.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.domain.student.exception.CreateStudentException;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.utility.id.Id;
import io.github.oosquare.neocourse.utility.id.IdGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentFactoryTest {

    private @Mock IdGenerator idGenerator;
    private @Mock StudentRepository studentRepository;
    private @InjectMocks StudentFactory studentFactory;

    @Test
    void createStudentAndTranscriptSucceeds() {
        when(this.idGenerator.generate())
            .thenReturn(Id.of("transcript0"), Id.of("student0"));
        when(this.studentRepository.findByUsername(any()))
            .thenReturn(Optional.empty());

        var result = this.studentFactory.createStudentAndTranscript(
            Username.of("test-student"),
            DisplayedUsername.of("test student"),
            new Plan(Id.of("plan0"), PlanName.of("test plan"))
        );
        var student = result.getStudent();
        var transcript = result.getTranscript();
        assertEquals(Id.of("student0"), student.getId());
        assertEquals(Id.of("transcript0"), transcript.getId());
        assertEquals(Id.of("transcript0"), student.getTranscript());
    }

    @Test
    void createStudentAndTranscriptThrowsWhenUsernameIsDuplicated() {
        var student = Student.builder()
            .id(Id.of("student0"))
            .username(Username.of("test-student"))
            .displayedUsername(DisplayedUsername.of("test student"))
            .plan(Id.of("plan0"))
            .transcript(Id.of("transcript0"))
            .build();
        when(this.studentRepository.findByUsername(any()))
            .thenReturn(Optional.of(student));

        assertThrows(CreateStudentException.class, () -> {
            this.studentFactory.createStudentAndTranscript(
                Username.of("test-student"),
                DisplayedUsername.of("test student"),
                new Plan(Id.of("plan0"), PlanName.of("test plan"))
            );
        });
    }
}