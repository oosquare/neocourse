package io.github.oosquare.neocourse.domain.schedule.model;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.utility.exception.ValueValidationException;

import static org.junit.jupiter.api.Assertions.*;

class PlaceTest {

    @Test
    public void createPlaceSucceeds() {
        var place = Place.of("example place");
        assertEquals("example place", place.getValue());
    }

    @Test
    public void createPlaceThrowsWhenParameterIsBlank() {
        assertThrows(ValueValidationException.class, () -> Place.of(""));
    }
}