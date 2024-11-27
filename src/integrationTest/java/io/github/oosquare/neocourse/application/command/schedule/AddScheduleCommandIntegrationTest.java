package io.github.oosquare.neocourse.application.command.schedule;

import java.time.ZonedDateTime;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.service.ScheduleRepository;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.utility.test.InitializeCourseTestSupport;
import io.github.oosquare.neocourse.utility.test.InitializeTeacherTestSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Getter
@SpringBootTest
@Transactional
public class AddScheduleCommandIntegrationTest
    implements
        InitializeTeacherTestSupport,
        InitializeCourseTestSupport {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CourseRepository courseRepository;
    @SpyBean
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ScheduleCommandService scheduleCommandService;

    @BeforeEach
    public void resetSpy() {
        reset(this.scheduleRepository);
    }

    @Test
    public void addScheduleSucceeds() {
        var course = this.createTestCourse();
        var startTime = ZonedDateTime.now().plusDays(1);

        var command = AddScheduleCommand.builder()
            .courseId(course.getId())
            .startTime(startTime)
            .place(Place.of("Test Place"))
            .capacity(Capacity.of(5))
            .build();
        var account = this.createTestTeacherAccount();

        this.scheduleCommandService.addSchedule(command, account);

        verify(this.scheduleRepository).save(assertArg(schedule -> {
            var res = this.scheduleRepository.findOrThrow(schedule.getId());
            assertEquals(startTime, res.getTime().getStart());
            assertEquals(Place.of("Test Place"), res.getPlace());
            assertEquals(Capacity.of(5), res.getCapacity());
        }));
    }
}
