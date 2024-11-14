package io.github.oosquare.neocourse.domain.account.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EncodedPasswordTest {

    @Test
    public void createSucceeds() {
        var password = EncodedPassword.of("encoded-password");
        assertEquals("encoded-password", password.getValue());
    }

    @Test
    public void createThrowsWhenEmpty() {
        assertThrows(IllegalArgumentException.class, () -> EncodedPassword.of(""));
    }

}