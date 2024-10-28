package io.github.oosquare.neocourse.domain.common.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsernameTest {

    @Test
    public void succeedWhenUsernameIsValid() {
        var username = new Username("a_valid-username01");
        assertEquals("a_valid-username01", username.getValue());
    }

    @Test
    public void throwWhenUsernameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Username(""));
        assertThrows(IllegalArgumentException.class, () -> new Username(" "));
    }

    @Test
    public void throwWhenUsernameContainsOtherCharacter() {
        assertThrows(IllegalArgumentException.class, () -> new Username(" leading-space"));
        assertThrows(IllegalArgumentException.class, () -> new Username("invalid-character!"));
    }
}