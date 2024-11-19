package io.github.oosquare.neocourse.domain.common.model;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.utility.exception.ValueValidationException;

import static org.junit.jupiter.api.Assertions.*;

class UsernameTest {

    @Test
    public void succeedWhenUsernameIsValid() {
        var username = Username.of("a_valid-username01");
        assertEquals("a_valid-username01", username.getValue());
    }

    @Test
    public void throwWhenUsernameIsBlank() {
        assertThrows(ValueValidationException.class, () -> Username.of(""));
        assertThrows(ValueValidationException.class, () -> Username.of(" "));
    }

    @Test
    public void throwWhenUsernameContainsOtherCharacter() {
        assertThrows(ValueValidationException.class, () -> Username.of(" leading-space"));
        assertThrows(ValueValidationException.class, () -> Username.of("invalid-character!"));
    }
}