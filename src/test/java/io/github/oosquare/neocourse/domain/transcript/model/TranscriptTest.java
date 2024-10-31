package io.github.oosquare.neocourse.domain.transcript.model;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TranscriptTest {

    @Test
    void assignScoreForCourse() {
        var transcript = Transcript.createInternally(Id.of("0"), Id.of("0"), new HashMap<>());
        transcript.assignScoreForCourse(Id.of("0"), Score.of(90));
        assertEquals(90, transcript.getCourseScores().get(Id.of("0")).getValue());
    }

    @Test
    void calculateFinalResultReturnsPlanUnfinished() {
        var transcript = Transcript.createInternally(
            Id.of("0"),
            Id.of("0"),
            new HashMap<>(Map.of(Id.of("0"), Score.of(90)))
        );
        var finalResult = transcript.calculateFinalResult(
            Map.of(Id.of("0"), ClassPeriod.of(1), Id.of("1"), ClassPeriod.of(1)),
            ClassPeriod.of(2)
        );
        assertEquals(FinalResult.ofPlanUnfinished(), finalResult);
    }

    @Test
    void calculateFinalResultReturnsPlanFinished() {
        var scores = Transcript.createInternally(
            Id.of("0"),
            Id.of("0"),
            new HashMap<>(Map.of(
                Id.of("0"),
                Score.of(90),
                Id.of("1"),
                Score.of(95),
                Id.of("2"),
                Score.of(100)
            ))
        );
        var finalResult = scores.calculateFinalResult(
            Map.of(
                Id.of("1"),
                ClassPeriod.of(1),
                Id.of("2"),
                ClassPeriod.of(1),
                Id.of("3"),
                ClassPeriod.of(1)
            ),
            ClassPeriod.of(2)
        );
        assertEquals(FinalResult.ofPlanFinished(97.5), finalResult);
    }
}