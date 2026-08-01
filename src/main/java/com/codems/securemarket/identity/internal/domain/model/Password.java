package com.codems.securemarket.identity.internal.domain.model;

import com.codems.securemarket.identity.internal.domain.exception.WeakPasswordException;
import java.util.Objects;

public final class Password {

    private static final int MIN_LENGTH = 12;
    private static final int MAX_LENGTH = 72;

    private final String value;

    public Password(String value) {
        if (value == null || value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new WeakPasswordException();
        }
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof Password password
                && value.equals(password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Password[PROTECTED]";
    }
}
