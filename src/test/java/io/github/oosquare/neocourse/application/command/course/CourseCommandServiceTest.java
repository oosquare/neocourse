package io.github.oosquare.neocourse.application.command.course;

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
import io.github.oosquare.neocourse.domain.course.service.CourseService;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseCommandServiceTest {

    private @Mock AccountService accountService;
    private @Mock CourseRepository courseRepository;
    private @Mock CourseService courseService;
    private @InjectMocks CourseCommandService courseCommandService;

    @Test
    public void addCourseSucceeds() {
        var command = AddCourseCommand.builder()
            .courseName(CourseName.of("Test Course"))
            .classPeriod(ClassPeriod.of(1))
            .build();
        var account = createTestAccount();
        var course = Course.builder()
            .id(Id.of("course0"))
            .name(command.getCourseName())
            .classPeriod(command.getClassPeriod())
            .build();

        doNothing().when(this.accountService).checkHasRole(account, AccountRoleKind.TEACHER);
        when(this.courseService.addCourse(command.getCourseName(), command.getClassPeriod()))
            .thenReturn(course);
        doNothing().when(this.courseRepository).save(course);

        this.courseCommandService.addCourse(command, account);
    }

    @Test
    public void removeCourseSucceeds() {
        var command = RemoveCourseCommand.builder()
            .courseId(Id.of("course0"))
            .build();
        var account = createTestAccount();
        var course = Course.builder()
            .id(Id.of("course0"))
            .name(CourseName.of("Test Course"))
            .classPeriod(ClassPeriod.of(1))
            .build();

        doNothing().when(this.accountService).checkHasRole(account, AccountRoleKind.TEACHER);
        when(this.courseRepository.findOrThrow(course.getId())).thenReturn(course);
        doNothing().when(this.courseService).prepareRemovingCourse(course);
        doNothing().when(this.courseRepository).remove(course);

        this.courseCommandService.removeCourse(command, account);
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
}