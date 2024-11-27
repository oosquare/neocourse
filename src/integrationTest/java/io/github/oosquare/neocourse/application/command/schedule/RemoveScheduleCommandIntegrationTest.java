package io.github.oosquare.neocourse.application.command.schedule;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.schedule.service.ScheduleRepository;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.utility.test.InitializeScheduleTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@Getter
@SpringBootTest
@Transactional
public class RemoveScheduleCommandIntegrationTest implements InitializeScheduleTestSupport {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ScheduleCommandService scheduleCommandService;

    @Test
    public void removeScheduleSucceeds() {
        var schedule = this.createTestSchedule();

        var command = RemoveScheduleCommand.builder()
            .scheduleId(schedule.getId())
            .build();
        var account = this.createTestTeacherAccount();

        this.scheduleCommandService.removeSchedule(command, account);

        assertTrue(this.scheduleRepository.find(schedule.getId()).isEmpty());
    }
}
