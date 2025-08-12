package com.adrianbcodes.currencyexchange.domain.currency;

import java.util.Objects;

public class CurrencyCode {
    private static final String FORMAT = "^[A-Z]{3}$";
    private final String code;

    public CurrencyCode(String code) {
        Objects.requireNonNull(code);
        String codeUpper = code.toUpperCase();
        if(!codeUpper.matches(FORMAT))
            throw new IllegalArgumentException("Invalid Currency Code: " + code);
        this.code = codeUpper;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyCode that = (CurrencyCode) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

    @Override
    public String toString() {
        return "CurrencyCode{" +
                "code='" + code + '\'' +
                '}';
    }
}
