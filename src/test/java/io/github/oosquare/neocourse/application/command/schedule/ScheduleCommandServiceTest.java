package io.github.oosquare.neocourse.application.command.schedule;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;

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
import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.schedule.model.TimeRange;
import io.github.oosquare.neocourse.domain.schedule.service.ScheduleRepository;
import io.github.oosquare.neocourse.domain.schedule.service.ScheduleService;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduleCommandServiceTest {

    private @Mock UserService userService;
    private @Mock ScheduleService scheduleService;
    private @Mock ScheduleRepository scheduleRepository;
    private @Mock CourseRepository courseRepository;
    private @InjectMocks ScheduleCommandService scheduleCommandService;

    @Test
    public void addScheduleSucceeds() {
        var account = createTestAccount();
        var teacher = createTestTeacher();
        var course = createTestCourse();
        var schedule = createTestSchedule();
        var command = AddScheduleCommand.builder()
            .courseId(course.getId())
            .startTime(schedule.getTime().getStart())
            .place(schedule.getPlace())
            .capacity(schedule.getCapacity())
            .build();

        when(this.userService.getTeacherUser(account)).thenReturn(teacher);
        when(this.courseRepository.findOrThrow(course.getId())).thenReturn(course);
        when(this.scheduleService.addNewSchedule(
            course,
            teacher,
            schedule.getTime().getStart(),
            schedule.getPlace(),
            schedule.getCapacity()
        )).thenReturn(schedule);
        doNothing().when(this.scheduleRepository).save(schedule);

        this.scheduleCommandService.addSchedule(command, account);
    }

    @Test
    public void removeSchedule() {
        var account = createTestAccount();
        var teacher = createTestTeacher();
        var schedule = createTestSchedule();
        var command = RemoveScheduleCommand.builder().scheduleId(schedule.getId()).build();

        when(this.userService.getTeacherUser(account)).thenReturn(teacher);
        when(this.scheduleRepository.findOrThrow(schedule.getId())).thenReturn(schedule);
        doNothing().when(this.scheduleService).prepareRemovingSchedule(schedule, teacher);
        doNothing().when(this.scheduleRepository).remove(schedule);

        this.scheduleCommandService.removeSchedule(command, account);
    }

    private static Account createTestAccount() {
        return Account.builder()
            .id(Id.of("account0"))
            .kind(AccountKind.TEACHER)
            .username(Username.of("test-teacher"))
            .displayedUsername(DisplayedUsername.of("Test Teacher"))
            .encodedPassword(EncodedPassword.of("encoded-password"))
            .user(Id.of("teacher0"))
            .build();
    }

    private static Teacher createTestTeacher() {
        return Teacher.builder()
            .id(Id.of("teacher0"))
            .username(Username.of("test-teacher"))
            .displayedUsername(DisplayedUsername.of("Test Teacher"))
            .build();
    }

    private static Course createTestCourse() {
        return Course.builder()
            .id(Id.of("course0"))
            .name(CourseName.of("Test Course"))
            .classPeriod(ClassPeriod.of(1))
            .build();
    }

    private static Schedule createTestSchedule() {
        return Schedule.builder()
            .id(Id.of("schedule0"))
            .course(Id.of("course0"))
            .teacher(Id.of("teacher0"))
            .time(TimeRange.of(ZonedDateTime.now(), Duration.ofMinutes(45)))
            .place(Place.of("place0"))
            .capacity(Capacity.of(1))
            .registrations(new HashMap<>())
            .build();
    }
}