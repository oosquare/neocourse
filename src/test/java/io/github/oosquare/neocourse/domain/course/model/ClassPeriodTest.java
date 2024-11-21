package io.github.oosquare.neocourse.domain.course.model;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.utility.exception.ValueValidationException;

import static org.junit.jupiter.api.Assertions.*;

class ClassPeriodTest {

    @Test
    public void createSucceeds() {
        var classPeriod = ClassPeriod.of(1);
        assertEquals(1, classPeriod.getValue());
    }

    @Test
    public void createThrowsWhenClassPeriodIsNotPositive() {
        assertThrows(ValueValidationException.class, () -> ClassPeriod.of(-1));
    }

    @Test
    public void plusSucceeds() {
        var period1 = ClassPeriod.of(1);
        var period2 = ClassPeriod.of(2);
        assertEquals(3, period1.plus(period2).getValue());
        assertEquals(3, period2.plus(period1).getValue());
    }

    @Test
    public void minusSucceeds() {
        var period1 = ClassPeriod.of(2);
        var period2 = ClassPeriod.of(1);
        assertEquals(1, period1.minus(period2).getValue());
    }
}