package com.adrianbcodes.currencyexchange.presentation.controller.dto;

import com.adrianbcodes.currencyexchange.domain.currency.CurrencyRate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public record CurrencyRateDto (String from, String to, BigDecimal rate, Instant date) {
    public CurrencyRateDto(String from, String to, BigDecimal rate, Instant date) {
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        this.rate = Objects.requireNonNull(rate);
        this.date = Objects.requireNonNull(date);
    }

    public static CurrencyRateDto fromDomain(CurrencyRate currencyRate) {
        return new CurrencyRateDto(
                currencyRate.codePair().from().code(),
                currencyRate.codePair().to().code(),
                currencyRate.rate().value(),
                currencyRate.date());
    }
}
