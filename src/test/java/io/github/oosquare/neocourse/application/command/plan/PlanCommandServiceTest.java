package io.github.oosquare.neocourse.application.command.plan;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.common.service.UserService;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.plan.model.CourseSet;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.domain.plan.service.PlanFactory;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanCommandServiceTest {

    private @Mock UserService userService;
    private @Mock PlanFactory planFactory;
    private @Mock PlanRepository planRepository;
    private @Mock CourseRepository courseRepository;
    private @InjectMocks PlanCommandService planCommandService;

    @Test
    public void addPlanSucceeds() {
        var command = AddPlanCommand.builder().planName(PlanName.of("Test Plan")).build();
        var account = createTestAccount();
        var plan = createTestPlan();

        doNothing().when(this.userService).checkIsUser(account, AccountKind.ADMINISTRATOR);
        when(this.planFactory.createPlan(command.getPlanName())).thenReturn(plan);
        doNothing().when(this.planRepository).save(plan);

        this.planCommandService.addPlan(command, account);
    }

    @Test
    public void includeCourseToPlan() {
        var account = createTestAccount();
        var plan = createTestPlan();
        var course = createTestCourse();
        var command = IncludeCourseCommand.builder()
            .planId(plan.getId())
            .courseId(course.getId())
            .build();

        doNothing().when(this.userService).checkIsUser(account, AccountKind.ADMINISTRATOR);
        when(this.courseRepository.findOrThrow(course.getId())).thenReturn(course);
        when(this.planRepository.findOrThrow(plan.getId())).thenReturn(plan);
        doNothing().when(this.planRepository).save(plan);

        this.planCommandService.includeCourseToPlan(command, account);
        assertTrue(plan.isCourseIncluded(course.getId()));
    }

    @Test
    public void excludeCourseFromPlan() {
        var account = createTestAccount();
        var course = createTestCourse();
        var plan = Plan.builder()
            .id(Id.of("plan0"))
            .name(PlanName.of("Test Plan"))
            .requiredClassPeriod(course.getClassPeriod())
            .includedCourses(CourseSet.ofInternally(Set.of(course.getId())))
            .build();
        var command = ExcludeCourseCommand.builder()
            .planId(plan.getId())
            .courseId(course.getId())
            .build();

        doNothing().when(this.userService).checkIsUser(account, AccountKind.ADMINISTRATOR);
        when(this.courseRepository.findOrThrow(course.getId())).thenReturn(course);
        when(this.planRepository.findOrThrow(plan.getId())).thenReturn(plan);
        doNothing().when(this.planRepository).save(plan);

        this.planCommandService.excludeCourseFromPlan(command, account);
        assertFalse(plan.isCourseIncluded(course.getId()));
    }

    private static Account createTestAccount() {
        return Account.builder()
            .id(Id.of("account0"))
            .kind(AccountKind.ADMINISTRATOR)
            .username(Username.of("test-account"))
            .displayedUsername(DisplayedUsername.of("Test Account"))
            .encodedPassword(EncodedPassword.of("encoded-password"))
            .user(Id.of("user0"))
            .build();
    }

    private static Plan createTestPlan() {
        return Plan.builder()
            .id(Id.of("plan0"))
            .name(PlanName.of("Test Plan"))
            .requiredClassPeriod(ClassPeriod.of(0))
            .includedCourses(CourseSet.of())
            .build();
    }

    private static Course createTestCourse() {
        return Course.builder()
            .id(Id.of("course0"))
            .name(CourseName.of("Test Course"))
            .classPeriod(ClassPeriod.of(1))
            .build();
    }
}