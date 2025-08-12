package com.adrianbcodes.currencyexchange.domain.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class ExchangeRate {
    private final BigDecimal rate;

    public ExchangeRate(BigDecimal rate) {
        Objects.requireNonNull(rate);
        if(rate.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Exchange rate must be positive");
        this.rate = rate.setScale(4, RoundingMode.UP);
    }

    public BigDecimal getRate() {
        return rate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate that = (ExchangeRate) o;
        return Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(rate);
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "rate=" + rate +
                '}';
    }
}
