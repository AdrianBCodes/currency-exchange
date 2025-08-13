package com.adrianbcodes.currencyexchange.domain.currency;

import java.util.Objects;

public record CurrencyCode(String code) {
    private static final String FORMAT = "^[A-Z]{3}$";

    public CurrencyCode(String code) {
        Objects.requireNonNull(code);
        String codeUpper = code.toUpperCase();
        if (!codeUpper.matches(FORMAT))
            throw new IllegalArgumentException("Invalid Currency Code: " + code);
        this.code = codeUpper;
    }
}
