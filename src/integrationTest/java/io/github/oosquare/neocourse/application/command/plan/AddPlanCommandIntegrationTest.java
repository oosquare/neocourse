package io.github.oosquare.neocourse.application.command.plan;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.utility.test.InitializeTeacherTestSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Getter
@SpringBootTest
@Transactional
public class AddPlanCommandIntegrationTest implements InitializeTeacherTestSupport {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private AccountRepository accountRepository;
    @SpyBean
    private PlanRepository planRepository;
    @Autowired
    private PlanCommandService planCommandService;

    @BeforeEach
    public void resetSpy() {
        reset(this.planRepository);
    }

    @Test
    public void addPlanSucceeds() {
        var command = AddPlanCommand.builder()
            .planName(PlanName.of("Test Plan"))
            .build();
        var account = this.createTestTeacherAccount();

        this.planCommandService.addPlan(command, account);

        verify(this.planRepository).save(assertArg(plan -> {
            var res = this.planRepository.findOrThrow(plan.getId());
            assertEquals(PlanName.of("Test Plan"), res.getName());
        }));
    }
}
