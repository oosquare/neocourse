package io.github.oosquare.neocourse.domain.schedule.model;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeRangeTest {

    private static final ZonedDateTime BASE_TIME = ZonedDateTime.ofInstant(
        Instant.ofEpochSecond(0),
        ZoneId.of("Z")
    );

    @Test
    void hasOverlap() {
        assertTrue(createRange(1, 2).hasOverlap(createRange(2, 2)));
        assertTrue(createRange(2, 2).hasOverlap(createRange(1, 2)));
        assertFalse(createRange(1, 1).hasOverlap(createRange(3, 1)));
        assertFalse(createRange(3, 1).hasOverlap(createRange(1, 1)));
        assertFalse(createRange(1, 1).hasOverlap(createRange(2, 1)));
    }

    TimeRange createRange(int start, int period) {
        return TimeRange.of(
            TimeRangeTest.BASE_TIME.plusSeconds(start),
            Duration.ofSeconds(period)
        );
    }
}