package com.adrianbcodes.currencyexchange.domain.currency;

import java.util.Objects;

public record CurrencyCodePair(CurrencyCode from, CurrencyCode to) {
    public CurrencyCodePair(CurrencyCode from, CurrencyCode to) {
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
    }

    public String toKey() {
        return String.format("%s:%s", from.code(), to.code());
    }
}
