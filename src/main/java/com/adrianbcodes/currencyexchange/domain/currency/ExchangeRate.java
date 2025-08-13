package com.adrianbcodes.currencyexchange.domain.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record ExchangeRate(BigDecimal value) {
    public ExchangeRate(BigDecimal value) {
        Objects.requireNonNull(value);
        if (value.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Exchange value must be positive");
        this.value = value.setScale(4, RoundingMode.UP);
    }
}
