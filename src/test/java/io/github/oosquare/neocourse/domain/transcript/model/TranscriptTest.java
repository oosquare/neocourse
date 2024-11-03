package io.github.oosquare.neocourse.domain.transcript.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.domain.transcript.exception.TranscriptException;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

class TranscriptTest {

    @Test
    void addCourse() {
        var transcript = Transcript.createInternally(
            Id.of("transcript0"),
            Id.of("plan0"),
            new HashMap<>()
        );
        transcript.addCourse(createTestCourse(0));
        assertTrue(transcript.getCourseScores().containsKey(Id.of("0")));
    }

    @Test
    void removeCourseIfNotGraded() {
        var transcript = Transcript.createInternally(
            Id.of("transcript0"),
            Id.of("plan0"),
            new HashMap<>(Map.of(
                Id.of("0"),
                TranscriptItem.of(Id.of("0"), ClassPeriod.of(1)),
                Id.of("1"),
                TranscriptItem.of(Id.of("1"), ClassPeriod.of(1)).assignScore(Score.of(60))
            ))
        );
        transcript.removeCourseIfNotGraded(createTestCourse(0));
        transcript.removeCourseIfNotGraded(createTestCourse(1));
        assertFalse(transcript.getCourseScores().containsKey(Id.of("0")));
        assertTrue(transcript.getCourseScores().containsKey(Id.of("1")));
    }

    @Test
    void gradeCourseSucceeds() {
        var transcript = Transcript.createInternally(
            Id.of("transcript0"),
            Id.of("plan0"),
            new HashMap<>(Map.of(
                Id.of("0"),
                TranscriptItem.of(Id.of("0"), ClassPeriod.of(1))
            ))
        );
        transcript.gradeCourse(createTestCourse(0), Score.of(60));
        assertEquals(Optional.of(Score.of(60)), transcript.getCourseScores().get(Id.of("0")).getScore());
        assertTrue(transcript.getCourseScores().get(Id.of("0")).isEvaluated());
    }

    @Test
    void gradeCourseThrowsWhenCourseIsNotIncludedInTranscript() {
        var transcript = Transcript.createInternally(
            Id.of("transcript0"),
            Id.of("plan0"),
            new HashMap<>()
        );
        assertThrows(TranscriptException.class, () -> {
            transcript.gradeCourse(createTestCourse(0), Score.of(60));
        });
    }

    @Test
    void isCourseSelectable() {
        var transcript = Transcript.createInternally(
            Id.of("0"),
            Id.of("0"),
            new HashMap<>(Map.of(
                Id.of("0"),
                TranscriptItem.of(Id.of("0"), ClassPeriod.of(1)),
                Id.of("1"),
                TranscriptItem.of(Id.of("1"), ClassPeriod.of(1)).assignScore(Score.of(95)),
                Id.of("2"),
                TranscriptItem.of(Id.of("2"), ClassPeriod.of(1)).markAbsent()
            ))
        );
        assertFalse(transcript.isCourseSelectable(createTestCourse(0)));
        assertFalse(transcript.isCourseSelectable(createTestCourse(1)));
        assertTrue(transcript.isCourseSelectable(createTestCourse(2)));
        assertTrue(transcript.isCourseSelectable(createTestCourse(3)));
    }

    @Test
    void calculateEstimatedClassPeriod() {
        var transcript = Transcript.createInternally(
            Id.of("0"),
            Id.of("0"),
            new HashMap<>(Map.of(
                Id.of("0"),
                TranscriptItem.of(Id.of("0"), ClassPeriod.of(1)),
                Id.of("1"),
                TranscriptItem.of(Id.of("1"), ClassPeriod.of(2)).assignScore(Score.of(95))
            ))
        );
        assertEquals(Optional.of(ClassPeriod.of(3)), transcript.calculateEstimatedClassPeriod());
    }

    @Test
    void calculateFinalResultReturnsPlanUnfinished() {
        var transcript = Transcript.createInternally(
            Id.of("transcript0"),
            Id.of("plan0"),
            new HashMap<>(Map.of(
                Id.of("0"),
                TranscriptItem.of(Id.of("0"), ClassPeriod.of(1)).assignScore(Score.of(90))
            ))
        );
        var finalResult = transcript.calculateFinalResult(ClassPeriod.of(2));
        assertEquals(FinalResult.ofPlanUnfinished(), finalResult);
    }

    @Test
    void calculateFinalResultReturnsPlanFinished() {
        var transcript = Transcript.createInternally(
            Id.of("0"),
            Id.of("0"),
            new HashMap<>(Map.of(
                Id.of("0"),
                TranscriptItem.of(Id.of("0"), ClassPeriod.of(1)),
                Id.of("1"),
                TranscriptItem.of(Id.of("1"), ClassPeriod.of(2)).assignScore(Score.of(95)),
                Id.of("2"),
                TranscriptItem.of(Id.of("2"), ClassPeriod.of(1)).assignScore(Score.of(100))
            ))
        );
        var finalResult = transcript.calculateFinalResult(ClassPeriod.of(4));
        assertEquals(FinalResult.ofPlanFinished(87.5), finalResult);
    }

    private static Course createTestCourse(int id) {
        return new Course(
            Id.of(String.format("%d", id)),
            CourseName.of(String.format("course %d", id)),
            ClassPeriod.of(1)
        );
    }
}