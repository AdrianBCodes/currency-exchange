package com.adrianbcodes.currencyexchange.domain.currency;

import java.time.Instant;
import java.util.Objects;

public class CurrencyRate {
    private final CurrencyCode from;
    private final CurrencyCode to;
    private final ExchangeRate rate;
    private final Instant date;

    public CurrencyRate(CurrencyCode from, CurrencyCode to, ExchangeRate rate, Instant date) {
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        this.rate = Objects.requireNonNull(rate);
        this.date = Objects.requireNonNull(date);
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

    public Instant getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyRate that = (CurrencyRate) o;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to) && Objects.equals(rate, that.rate) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, rate, date);
    }

    @Override
    public String toString() {
        return "CurrencyRate{" +
                "from=" + from +
                ", to=" + to +
                ", value=" + rate +
                ", date=" + date +
                '}';
    }
}
