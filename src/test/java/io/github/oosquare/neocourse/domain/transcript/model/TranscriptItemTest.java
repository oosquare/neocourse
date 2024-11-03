package io.github.oosquare.neocourse.domain.transcript.model;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

class TranscriptItemTest {

    @Test
    void accountsForEstimatedClassPeriod() {
        var item = TranscriptItem.of(Id.of("course0"), ClassPeriod.of(1));
        assertFalse(item.isGraded());

        item = TranscriptItem.of(Id.of("course0"), ClassPeriod.of(1)).assignScore(Score.of(60));
        assertTrue(item.isGraded());
    }
}