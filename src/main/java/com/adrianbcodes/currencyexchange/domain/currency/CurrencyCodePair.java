package com.adrianbcodes.currencyexchange.domain.currency;

import java.util.Objects;

public class CurrencyCodePair {
    private final CurrencyCode from;
    private final CurrencyCode to;

    public CurrencyCodePair(CurrencyCode from, CurrencyCode to) {
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyCodePair that = (CurrencyCodePair) o;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "CurrencyCodePair{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
