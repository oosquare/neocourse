package io.github.oosquare.neocourse.application.command.registration;

import java.util.Set;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.plan.model.CourseSet;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.domain.schedule.service.ScheduleRepository;
import io.github.oosquare.neocourse.domain.student.service.StudentRepository;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.domain.transcript.service.TranscriptRepository;
import io.github.oosquare.neocourse.utility.id.Id;
import io.github.oosquare.neocourse.utility.test.InitializeScheduleTestSupport;
import io.github.oosquare.neocourse.utility.test.InitializeStudentTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@Getter
@SpringBootTest
@Transactional
public class AddRegistrationCommandIntegrationTest
    implements
        InitializeScheduleTestSupport,
        InitializeStudentTestSupport {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private TranscriptRepository transcriptRepository;
    @Autowired
    private RegistrationCommandService registrationCommandService;

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
    public void addRegistrationSucceeds() {
        var student = this.createTestStudent();
        var schedule = this.createTestSchedule();

        var command = AddRegistrationCommand.builder()
            .scheduleId(schedule.getId())
            .build();
        var account = this.createTestStudentAccount();

        this.registrationCommandService.addRegistration(command, account);

        var res = this.scheduleRepository.findOrThrow(schedule.getId());
        assertTrue(res.isStudentRegistered(student));
    }
}
