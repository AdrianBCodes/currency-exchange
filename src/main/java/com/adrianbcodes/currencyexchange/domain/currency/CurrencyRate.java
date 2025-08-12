package com.adrianbcodes.currencyexchange.domain.currency;

import java.util.Objects;

public class CurrencyRate {
    private final CurrencyCode code;
    private final ExchangeRate rate;

    public CurrencyRate(CurrencyCode code, ExchangeRate rate) {
        this.code = Objects.requireNonNull(code);
        this.rate = Objects.requireNonNull(rate);
    }

    public CurrencyCode getCode() {
        return code;
    }

    public ExchangeRate getRate() {
        return rate;
    }
}
