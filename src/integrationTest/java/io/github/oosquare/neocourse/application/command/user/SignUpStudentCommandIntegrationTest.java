package io.github.oosquare.neocourse.application.command.user;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.domain.student.service.StudentRepository;
import io.github.oosquare.neocourse.domain.transcript.service.TranscriptRepository;
import io.github.oosquare.neocourse.utility.test.InitializePlanTestSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Getter
@SpringBootTest
@Transactional
public class SignUpStudentCommandIntegrationTest implements InitializePlanTestSupport {

    @Autowired
    private PlanRepository planRepository;
    @SpyBean
    private StudentRepository studentRepository;
    @Autowired
    private TranscriptRepository transcriptRepository;
    @Autowired
    private UserCommandService userCommandService;

    @BeforeEach
    public void resetSpy() {
        reset(this.studentRepository);
    }

    @Test
    public void signUpStudentSucceeds() {
        var plan = this.createTestPlan();

        var command = SignUpStudentCommand.builder()
            .username(Username.of("test-student"))
            .displayedUsername(DisplayedUsername.of("Test Student"))
            .encodedPassword(EncodedPassword.of("encoded-password"))
            .planId(plan.getId())
            .build();

        this.userCommandService.signUpStudent(command);

        verify(this.studentRepository).save(assertArg(student -> {
            var studentRes = this.studentRepository.findOrThrow(student.getId());
            var transcriptRes = this.transcriptRepository.findOrThrow(student.getTranscript());

            assertEquals(Username.of("test-student"), studentRes.getUsername());
            assertEquals(DisplayedUsername.of("Test Student"), studentRes.getDisplayedUsername());
            assertEquals(plan.getId(), studentRes.getPlan());
            assertEquals(studentRes.getTranscript(), transcriptRes.getId());
        }));
    }
}
