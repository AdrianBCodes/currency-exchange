package com.adrianbcodes.currencyexchange.infrastructure.nbp.response;

import java.math.BigDecimal;

public record NbpCurrencyRate(String currency, String code, BigDecimal mid) {
}
