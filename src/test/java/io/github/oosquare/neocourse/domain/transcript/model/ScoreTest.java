package io.github.oosquare.neocourse.domain.transcript.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    @Test
    public void succeedIfScoreIsInRange() {
        assertEquals(0, (Score.of(0)).getValue());
        assertEquals(90, (Score.of(90)).getValue());
        assertEquals(100, (Score.of(100)).getValue());
    }

    @Test
    public void throwIfScoreIsOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> Score.of(-1));
        assertThrows(IllegalArgumentException.class, () -> Score.of(101));
    }
}