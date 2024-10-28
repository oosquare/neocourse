package io.github.oosquare.neocourse.domain.student.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    @Test
    public void succeedIfScoreIsInRange() {
        assertEquals(0, (new Score(0)).getScore());
        assertEquals(90, (new Score(90)).getScore());
        assertEquals(100, (new Score(100)).getScore());
    }

    @Test
    public void throwIfScoreIsOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> new Score(-1));
        assertThrows(IllegalArgumentException.class, () -> new Score(101));
    }
}