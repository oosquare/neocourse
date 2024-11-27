package io.github.oosquare.neocourse.application.command.plan;

import java.util.Set;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.admin.service.AdministratorRepository;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.plan.model.CourseSet;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.utility.id.Id;
import io.github.oosquare.neocourse.utility.test.InitializeAdministratorTestSupport;
import io.github.oosquare.neocourse.utility.test.InitializeCourseTestSupport;
import io.github.oosquare.neocourse.utility.test.InitializePlanTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@Getter
@SpringBootTest
@Transactional
public class ExcludeCourseCommandIntegrationTest
    implements
        InitializeAdministratorTestSupport,
        InitializeCourseTestSupport,
        InitializePlanTestSupport {

    @Autowired
    private AdministratorRepository administratorRepository;
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
    public void excludeCourseFromPlanSucceeds() {
        var plan = this.createTestPlan();
        var course = this.createTestCourse();

        var command = ExcludeCourseCommand.builder()
            .planId(plan.getId())
            .courseId(course.getId())
            .build();
        var account = this.createTestAdministratorAccount();

        this.planCommandService.excludeCourseFromPlan(command, account);

        var res = this.planRepository.findOrThrow(plan.getId());
        assertFalse(res.isCourseIncluded(course.getId()));
    }
}
