package com.adrianbcodes.currencyexchange.application.port.out;

import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCode;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyRate;

import java.util.Optional;

public interface CurrencyRateProvider {
    Optional<CurrencyRate> getRate(CurrencyCode code);
}
