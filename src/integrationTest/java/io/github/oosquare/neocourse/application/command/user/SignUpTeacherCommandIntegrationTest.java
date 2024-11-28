package io.github.oosquare.neocourse.application.command.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class SignUpTeacherCommandIntegrationTest {

    @SpyBean
    private TeacherRepository teacherRepository;
    @Autowired
    private UserCommandService userCommandService;

    @BeforeEach
    public void resetSpy() {
        reset(this.teacherRepository);
    }

    @Test
    public void signUpTeacherSucceeds() {
        var command = SignUpTeacherCommand.builder()
            .username(Username.of("test-teacher"))
            .displayedUsername(DisplayedUsername.of("Test Teacher"))
            .encodedPassword(EncodedPassword.of("encoded-password"))
            .build();

        this.userCommandService.signUpTeacher(command);

        verify(this.teacherRepository).save(assertArg(teacher -> {
            var res = this.teacherRepository.findOrThrow(teacher.getId());
            assertEquals(Username.of("test-teacher"), res.getUsername());
            assertEquals(DisplayedUsername.of("Test Teacher"), res.getDisplayedUsername());
        }));
    }
}
