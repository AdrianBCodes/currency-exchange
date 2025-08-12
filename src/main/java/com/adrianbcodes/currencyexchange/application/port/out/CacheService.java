package com.adrianbcodes.currencyexchange.application.port.out;

import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCode;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCodePair;
import com.adrianbcodes.currencyexchange.domain.currency.ExchangeRate;

import java.util.Map;
import java.util.Optional;

public interface CacheService {
    Optional<ExchangeRate> getRate(CurrencyCode from, CurrencyCode to);
    void putRate(CurrencyCode from, CurrencyCode to, ExchangeRate rate);
    void putRates(Map<CurrencyCodePair, ExchangeRate> rates);
}
