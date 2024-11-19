package io.github.oosquare.neocourse.domain.course.model;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.utility.exception.ValueValidationException;

import static org.junit.jupiter.api.Assertions.*;

class ClassPeriodTest {

    @Test
    public void createSucceeds() {
        var classHours = ClassPeriod.of(1);
        assertEquals(1, classHours.getValue());
    }

    @Test
    public void createThrowsWhenClassPeriodIsNotPositive() {
        assertThrows(ValueValidationException.class, () -> ClassPeriod.of(0));
        assertThrows(ValueValidationException.class, () -> ClassPeriod.of(-1));
    }
}