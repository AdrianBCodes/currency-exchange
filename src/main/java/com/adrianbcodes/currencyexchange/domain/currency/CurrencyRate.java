package com.adrianbcodes.currencyexchange.domain.currency;

import java.time.Instant;
import java.util.Objects;

public record CurrencyRate(CurrencyCodePair codePair, ExchangeRate rate, Instant date) {
    public CurrencyRate(CurrencyCodePair codePair, ExchangeRate rate, Instant date) {
        this.codePair = Objects.requireNonNull(codePair);
        this.rate = Objects.requireNonNull(rate);
        this.date = Objects.requireNonNull(date);
    }
}
