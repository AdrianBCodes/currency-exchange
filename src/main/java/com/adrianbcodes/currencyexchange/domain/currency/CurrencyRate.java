package com.adrianbcodes.currencyexchange.domain.currency;

import java.util.Objects;

public class CurrencyRate {
    private final CurrencyCode from;
    private final CurrencyCode to;
    private final ExchangeRate rate;

    public CurrencyRate(CurrencyCode from, CurrencyCode to, ExchangeRate rate) {
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        this.rate = Objects.requireNonNull(rate);
    }

    public CurrencyCode getFrom() {
        return from;
    }

    public CurrencyCode getTo() {
        return to;
    }

    public ExchangeRate getRate() {
        return rate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyRate that = (CurrencyRate) o;
        return Objects.equals(to, that.to) && Objects.equals(from, that.from) && Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, rate);
    }

    @Override
    public String toString() {
        return "CurrencyRate{" +
                "from=" + from +
                ", to=" + to +
                ", rate=" + rate +
                '}';
    }
}
