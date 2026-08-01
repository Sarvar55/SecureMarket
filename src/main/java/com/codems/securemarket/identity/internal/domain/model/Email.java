package com.codems.securemarket.identity.internal.domain.model;

import com.codems.securemarket.identity.internal.domain.exception.InvalidEmailException;
import java.util.Locale;
import java.util.regex.Pattern;

public record Email(String value) {

    private static final int MAX_LENGTH = 254;
    private static final Pattern FORMAT = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    public Email {
        if (value == null) {
            throw new InvalidEmailException();
        }

        value = value.trim().toLowerCase(Locale.ROOT);

        if (value.isBlank() || value.length() > MAX_LENGTH || !FORMAT.matcher(value).matches()) {
            throw new InvalidEmailException();
        }
    }
}
