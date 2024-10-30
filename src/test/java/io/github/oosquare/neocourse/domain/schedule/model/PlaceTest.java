package io.github.oosquare.neocourse.domain.schedule.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceTest {

    @Test
    public void createPlaceSucceeds() {
        var place = new Place("example place");
        assertEquals("example place", place.getValue());
    }

    @Test
    public void createPlaceThrowsWhenParameterIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Place(""));
    }
}