package io.github.oosquare.neocourse.application.command.evaluation;

import java.util.Map;
import java.util.Set;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.plan.model.CourseSet;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.Registration;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.schedule.model.TimeRange;
import io.github.oosquare.neocourse.domain.schedule.service.ScheduleRepository;
import io.github.oosquare.neocourse.domain.student.service.StudentRepository;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.domain.transcript.model.Transcript;
import io.github.oosquare.neocourse.domain.transcript.model.TranscriptItem;
import io.github.oosquare.neocourse.domain.transcript.service.TranscriptRepository;
import io.github.oosquare.neocourse.utility.id.Id;
import io.github.oosquare.neocourse.utility.test.InitializeScheduleTestSupport;
import io.github.oosquare.neocourse.utility.test.InitializeStudentTestSupport;

@Getter
public abstract class AbstractEvaluationCommandServiceIntegrationTest
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
    protected EvaluationCommandService evaluationCommandService;

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

    @Override
    public Schedule createTestSchedule() {
        var course = this.createTestCourse();
        var student = this.createTestStudent();
        return Schedule.builder()
            .id(Id.of("test-schedule"))
            .course(course.getId())
            .teacher(this.createTestTeacher().getId())
            .time(TimeRange.of(TEST_START_TIME.minusDays(2), course.getActualPeriod()))
            .place(Place.of("Test Place"))
            .capacity(Capacity.of(5))
            .registrations(Map.of(student.getId(), Registration.of(student.getId())))
            .build();
    }

    @Override
    public Transcript createTestTranscript() {
        var course = this.createTestCourse();
        return Transcript.builder()
            .id(Id.of("test-transcript"))
            .plan(this.createTestPlan().getId())
            .courseScores(Map.of(
                course.getId(),
                TranscriptItem.of(course.getId(), course.getClassPeriod())
            ))
            .build();
    }
}
