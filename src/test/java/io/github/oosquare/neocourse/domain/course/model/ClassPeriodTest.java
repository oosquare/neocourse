package io.github.oosquare.neocourse.domain.course.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassPeriodTest {

    @Test
    public void createSucceeds() {
        var classHours = ClassPeriod.of(1);
        assertEquals(1, classHours.getValue());
    }

    @Test
    public void createThrowsWhenClassPeriodIsNotPositive() {
        assertThrows(IllegalArgumentException.class, () -> ClassPeriod.of(0));
        assertThrows(IllegalArgumentException.class, () -> ClassPeriod.of(-1));
    }
}