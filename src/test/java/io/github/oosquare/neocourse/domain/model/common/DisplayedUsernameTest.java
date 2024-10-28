package io.github.oosquare.neocourse.domain.model.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisplayedUsernameTest {

    @Test
    public void succeedWhenDisplayedUsernameIsValid() {
        var displayedUsername = new DisplayedUsername("Example Username");
        assertEquals("Example Username", displayedUsername.getValue());
    }

    @Test
    public void throwWhenDisplayedUsernameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new DisplayedUsername(""));
        assertThrows(IllegalArgumentException.class, () -> new DisplayedUsername(" "));
    }

    @Test
    public void throwWhenDisplayedUsernameHasLeadingOrTrailingSpaces() {
        assertThrows(IllegalArgumentException.class, () -> new DisplayedUsername(" leading-space"));
        assertThrows(IllegalArgumentException.class, () -> new DisplayedUsername("trailing-space "));
        assertThrows(IllegalArgumentException.class, () -> new DisplayedUsername(" both "));
    }
}