package io.github.oosquare.neocourse.application.command.plan;

import java.util.Set;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.plan.model.CourseSet;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.utility.id.Id;
import io.github.oosquare.neocourse.utility.test.InitializeCourseTestSupport;
import io.github.oosquare.neocourse.utility.test.InitializePlanTestSupport;
import io.github.oosquare.neocourse.utility.test.InitializeTeacherTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@Getter
@SpringBootTest
@Transactional
public class AssignRequiredClassPeriodCommandIntegrationTest
    implements
        InitializeTeacherTestSupport,
        InitializeCourseTestSupport,
        InitializePlanTestSupport {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PlanCommandService planCommandService;

    @Override
    public Plan createTestPlan() {
        var course = this.createTestCourse();
        return Plan.builder()
            .id(Id.of("test-plan"))
            .name(PlanName.of("Test Plan"))
            .totalClassPeriod(course.getClassPeriod())
            .requiredClassPeriod(course.getClassPeriod())
            .includedCourses(CourseSet.ofInternally(Set.of(course.getId())))
            .build();
    }

    @Test
    public void assignRequiredClassPeriodSucceeds() {
        var plan = this.createTestPlan();

        var command = AssignRequiredClassPeriodCommand.builder()
            .planId(plan.getId())
            .requiredClassPeriod(ClassPeriod.of(1))
            .build();
        var account = this.createTestTeacherAccount();

        this.planCommandService.assignRequiredClassPeriod(command, account);

        var res = this.planRepository.findOrThrow(plan.getId());
        assertEquals(ClassPeriod.of(1), res.getRequiredClassPeriod());
    }
}
