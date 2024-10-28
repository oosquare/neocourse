package io.github.oosquare.neocourse.domain.course.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassPeriodTest {

    @Test
    public void createSucceeds() {
        var classHours = new ClassPeriod(1);
        assertEquals(1, classHours.getValue());
    }

    @Test
    public void createThrowsWhenClassPeriodIsNotPositive() {
        assertThrows(IllegalArgumentException.class, () -> new ClassPeriod(0));
        assertThrows(IllegalArgumentException.class, () -> new ClassPeriod(-1));
    }
}