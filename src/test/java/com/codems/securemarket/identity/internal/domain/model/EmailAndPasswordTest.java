package com.codems.securemarket.identity.internal.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.codems.securemarket.identity.internal.domain.exception.InvalidEmailException;
import com.codems.securemarket.identity.internal.domain.exception.WeakPasswordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailAndPasswordTest {

    @Test
    @DisplayName("When email is normalized")
    void whenEmailIsNormalizedThenReturnsNormalizedEmail() {
        assertEquals("user@example.com", new Email(" USER@Example.com ").value());
    }

    @Test
    @DisplayName("When invalid email then throws exception")
    void whenInvalidEmailThenThrowsException() {
        assertThrows(InvalidEmailException.class, () -> new Email("invalid"));
    }

    @Test
    @DisplayName("When short password then throws exception")
    void whenShortPasswordThenThrowsException() {
        assertThrows(WeakPasswordException.class, () -> new Password("short"));
    }
}
