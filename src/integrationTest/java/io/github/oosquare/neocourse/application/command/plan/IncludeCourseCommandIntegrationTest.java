package io.github.oosquare.neocourse.application.command.plan;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.utility.test.InitializeCourseTestSupport;
import io.github.oosquare.neocourse.utility.test.InitializePlanTestSupport;
import io.github.oosquare.neocourse.utility.test.InitializeTeacherTestSupport;

import static org.junit.jupiter.api.Assertions.*;


@Getter
@SpringBootTest
@Transactional
public class IncludeCourseCommandIntegrationTest
    implements
        InitializeTeacherTestSupport,
        InitializePlanTestSupport,
        InitializeCourseTestSupport {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private PlanCommandService planCommandService;

    @Test
    public void includeCourseToPlanSucceeds() {
        var plan = this.createTestPlan();
        var course = this.createTestCourse();

        var command = IncludeCourseCommand.builder()
            .planId(plan.getId())
            .courseId(course.getId())
            .build();
        var account = this.createTestTeacherAccount();

        this.planCommandService.includeCourseToPlan(command, account);

        var res = this.planRepository.findOrThrow(plan.getId());
        assertTrue(res.isCourseIncluded(course.getId()));
    }
}
