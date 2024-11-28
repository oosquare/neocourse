package io.github.oosquare.neocourse.application.command.evaluation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.transcript.model.Score;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GradeStudentCommandIntegrationTest
    extends AbstractEvaluationCommandServiceIntegrationTest {

    @Test
    public void gradeStudentSucceeds() {
        var student = this.createTestStudent();
        var schedule = this.createTestSchedule();
        var course = this.createTestCourse();

        var command = GradeStudentCommand.builder()
            .studentId(student.getId())
            .scheduleId(schedule.getId())
            .score(Score.of(90))
            .build();
        var account = this.createTestTeacherAccount();

        this.evaluationCommandService.gradeStudent(command, account);

        var res = this.getTranscriptRepository().findOrThrow(student.getTranscript());
        assertEquals(Score.of(90), res.getScore(course).orElseThrow());
        var item = res.getCourseScores().get(course.getId());
        assertTrue(item.isEvaluated());
        assertFalse(item.isParticipationAbsent());
    }
}
