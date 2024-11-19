package io.github.oosquare.neocourse.domain.common.model;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.utility.exception.ValueValidationException;

import static org.junit.jupiter.api.Assertions.*;

class DisplayedUsernameTest {

    @Test
    public void succeedWhenDisplayedUsernameIsValid() {
        var displayedUsername = DisplayedUsername.of("Example Username");
        assertEquals("Example Username", displayedUsername.getValue());
    }

    @Test
    public void throwWhenDisplayedUsernameIsBlank() {
        assertThrows(ValueValidationException.class, () -> DisplayedUsername.of(""));
        assertThrows(ValueValidationException.class, () -> DisplayedUsername.of(" "));
    }

    @Test
    public void throwWhenDisplayedUsernameHasLeadingOrTrailingSpaces() {
        assertThrows(ValueValidationException.class, () -> DisplayedUsername.of(" leading-space"));
        assertThrows(ValueValidationException.class, () -> DisplayedUsername.of("trailing-space "));
        assertThrows(ValueValidationException.class, () -> DisplayedUsername.of(" both "));
    }
}