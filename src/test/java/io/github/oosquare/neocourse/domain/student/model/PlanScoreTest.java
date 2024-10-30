package io.github.oosquare.neocourse.domain.student.model;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlanScoreTest {

    @Test
    void assignScoreForCourse() {
        var scores = PlanScore.of();
        var newScores = scores.assignScoreForCourse(new Id("0"), Score.of(90));
        assertEquals(90, newScores.getCourseScores().get(new Id("0")).getValue());
    }

    @Test
    void calculateFinalResultReturnsPlanUnfinished() {
        var scores = PlanScore.of()
            .assignScoreForCourse(new Id("0"), Score.of(90));
        var finalResult = scores.calculateFinalResult(
            Map.of(new Id("0"), ClassPeriod.of(1), new Id("1"), ClassPeriod.of(1)),
            ClassPeriod.of(2)
        );
        assertEquals(FinalResult.ofPlanUnfinished(), finalResult);
    }

    @Test
    void calculateFinalResultReturnsPlanFinished() {
        var scores = PlanScore.of()
            .assignScoreForCourse(new Id("0"), Score.of(90))
            .assignScoreForCourse(new Id("1"), Score.of(95))
            .assignScoreForCourse(new Id("2"), Score.of(100));
        var finalResult = scores.calculateFinalResult(
            Map.of(
                new Id("1"),
                ClassPeriod.of(1),
                new Id("2"),
                ClassPeriod.of(1),
                new Id("3"),
                ClassPeriod.of(1)
            ),
            ClassPeriod.of(2)
        );
        assertEquals(FinalResult.ofPlanFinished(97.5), finalResult);
    }
}