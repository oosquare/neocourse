package io.github.oosquare.neocourse.domain.common.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisplayedUsernameTest {

    @Test
    public void succeedWhenDisplayedUsernameIsValid() {
        var displayedUsername = DisplayedUsername.of("Example Username");
        assertEquals("Example Username", displayedUsername.getValue());
    }

    @Test
    public void throwWhenDisplayedUsernameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> DisplayedUsername.of(""));
        assertThrows(IllegalArgumentException.class, () -> DisplayedUsername.of(" "));
    }

    @Test
    public void throwWhenDisplayedUsernameHasLeadingOrTrailingSpaces() {
        assertThrows(IllegalArgumentException.class, () -> DisplayedUsername.of(" leading-space"));
        assertThrows(IllegalArgumentException.class, () -> DisplayedUsername.of("trailing-space "));
        assertThrows(IllegalArgumentException.class, () -> DisplayedUsername.of(" both "));
    }
}