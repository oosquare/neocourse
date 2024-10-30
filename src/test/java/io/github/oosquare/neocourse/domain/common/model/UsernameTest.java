package io.github.oosquare.neocourse.domain.common.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsernameTest {

    @Test
    public void succeedWhenUsernameIsValid() {
        var username = Username.of("a_valid-username01");
        assertEquals("a_valid-username01", username.getValue());
    }

    @Test
    public void throwWhenUsernameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> Username.of(""));
        assertThrows(IllegalArgumentException.class, () -> Username.of(" "));
    }

    @Test
    public void throwWhenUsernameContainsOtherCharacter() {
        assertThrows(IllegalArgumentException.class, () -> Username.of(" leading-space"));
        assertThrows(IllegalArgumentException.class, () -> Username.of("invalid-character!"));
    }
}