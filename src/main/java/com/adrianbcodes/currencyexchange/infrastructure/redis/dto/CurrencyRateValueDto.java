package com.adrianbcodes.currencyexchange.infrastructure.redis.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public record CurrencyRateValueDto(BigDecimal rate, Instant date) implements Serializable {
}
