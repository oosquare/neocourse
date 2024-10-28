package io.github.oosquare.neocourse.domain.model.student;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    @Test
    public void succeedIfScoreIsInRange() {
        assertEquals(0, (new Score(0)).getValue());
        assertEquals(90, (new Score(90)).getValue());
        assertEquals(100, (new Score(100)).getValue());
    }

    @Test
    public void throwIfScoreIsOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> new Score(-1));
        assertThrows(IllegalArgumentException.class, () -> new Score(101));
    }
}