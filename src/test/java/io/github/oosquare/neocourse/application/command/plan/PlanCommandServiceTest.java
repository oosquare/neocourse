package io.github.oosquare.neocourse.application.command.plan;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountRole;
import io.github.oosquare.neocourse.domain.account.model.AccountRoleKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.account.service.AccountService;
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

    private @Mock AccountService accountService;
    private @Mock PlanFactory planFactory;
    private @Mock PlanRepository planRepository;
    private @Mock CourseRepository courseRepository;
    private @InjectMocks PlanCommandService planCommandService;

    @Test
    public void addPlanSucceeds() {
        var command = AddPlanCommand.builder().planName(PlanName.of("Test Plan")).build();
        var account = createTestAccount();
        var plan = createTestPlan();

        doNothing().when(this.accountService).checkHasRole(account, AccountRoleKind.TEACHER);
        when(this.planFactory.createPlan(command.getPlanName())).thenReturn(plan);
        doNothing().when(this.planRepository).save(plan);

        this.planCommandService.addPlan(command, account);
    }

    @Test
    public void includeCourseToPlanSucceeds() {
        var account = createTestAccount();
        var plan = createTestPlan();
        var course = createTestCourse();
        var command = IncludeCourseCommand.builder()
            .planId(plan.getId())
            .courseId(course.getId())
            .build();

        doNothing().when(this.accountService).checkHasRole(account, AccountRoleKind.TEACHER);
        when(this.courseRepository.findOrThrow(course.getId())).thenReturn(course);
        when(this.planRepository.findOrThrow(plan.getId())).thenReturn(plan);
        doNothing().when(this.planRepository).save(plan);

        this.planCommandService.includeCourseToPlan(command, account);
        assertTrue(plan.isCourseIncluded(course.getId()));
    }

    @Test
    public void excludeCourseFromPlanSucceeds() {
        var account = createTestAccount();
        var course = createTestCourse();
        var plan = Plan.builder()
            .id(Id.of("plan0"))
            .name(PlanName.of("Test Plan"))
            .totalClassPeriod(course.getClassPeriod())
            .requiredClassPeriod(course.getClassPeriod())
            .includedCourses(CourseSet.ofInternally(Set.of(course.getId())))
            .build();
        var command = ExcludeCourseCommand.builder()
            .planId(plan.getId())
            .courseId(course.getId())
            .build();

        doNothing().when(this.accountService).checkHasRole(account, AccountRoleKind.TEACHER);
        when(this.courseRepository.findOrThrow(course.getId())).thenReturn(course);
        when(this.planRepository.findOrThrow(plan.getId())).thenReturn(plan);
        doNothing().when(this.planRepository).save(plan);

        this.planCommandService.excludeCourseFromPlan(command, account);
        assertFalse(plan.isCourseIncluded(course.getId()));
    }

    @Test
    public void assignRequiredClassPeriodSucceeds() {
        var account = createTestAccount();
        var course = createTestCourse();
        var plan = Plan.builder()
            .id(Id.of("plan0"))
            .name(PlanName.of("Test Plan"))
            .totalClassPeriod(course.getClassPeriod())
            .requiredClassPeriod(ClassPeriod.of(0))
            .includedCourses(CourseSet.ofInternally(Set.of(course.getId())))
            .build();
        var command = AssignRequiredClassPeriodCommand.builder()
            .planId(plan.getId())
            .requiredClassPeriod(course.getClassPeriod())
            .build();

        doNothing().when(this.accountService).checkHasRole(account, AccountRoleKind.TEACHER);
        when(this.planRepository.findOrThrow(plan.getId())).thenReturn(plan);
        doNothing().when(this.planRepository).save(plan);

        this.planCommandService.assignRequiredClassPeriod(command, account);
        assertEquals(course.getClassPeriod(), plan.getRequiredClassPeriod());
    }

    private static Account createTestAccount() {
        return Account.builder()
            .id(Id.of("account0"))
            .username(Username.of("test-account"))
            .displayedUsername(DisplayedUsername.of("Test Account"))
            .encodedPassword(EncodedPassword.of("encoded-password"))
            .role(AccountRoleKind.TEACHER, AccountRole.of(AccountRoleKind.TEACHER, Id.of("user0")))
            .build();
    }

    private static Plan createTestPlan() {
        return Plan.builder()
            .id(Id.of("plan0"))
            .name(PlanName.of("Test Plan"))
            .totalClassPeriod(ClassPeriod.of(0))
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