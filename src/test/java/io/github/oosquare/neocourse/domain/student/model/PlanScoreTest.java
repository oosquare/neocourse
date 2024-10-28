package io.github.oosquare.neocourse.domain.student.model;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

class PlanScoreTest {

    @Test
    void testAssignScoreForCourse() {
        var scores = new PlanScore();
        var newScores = scores.assignScoreForCourse(new Id("0"), new Score(90));
        assertEquals(90, newScores.getCourseScores().get(new Id("0")).getScore());
    }
}