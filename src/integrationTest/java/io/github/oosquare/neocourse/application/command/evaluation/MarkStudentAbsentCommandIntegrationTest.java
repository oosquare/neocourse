package io.github.oosquare.neocourse.application.command.evaluation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MarkStudentAbsentCommandIntegrationTest
    extends AbstractEvaluationCommandServiceIntegrationTest {

    @Test
    public void markStudentAbsentSucceeds() {
        var student = this.createTestStudent();
        var schedule = this.createTestSchedule();
        var course = this.createTestCourse();

        var command = MarkStudentAbsentCommand.builder()
            .studentId(student.getId())
            .scheduleId(schedule.getId())
            .build();
        var account = this.createTestTeacherAccount();

        this.evaluationCommandService.markStudentAbsent(command, account);

        var res = this.getTranscriptRepository().findOrThrow(student.getTranscript());
        var item = res.getCourseScores().get(course.getId());
        assertTrue(item.isEvaluated());
        assertTrue(item.isParticipationAbsent());
    }
}
