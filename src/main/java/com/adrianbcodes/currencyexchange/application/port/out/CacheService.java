package com.adrianbcodes.currencyexchange.application.port.out;

import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCodePair;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyRate;

import java.util.Map;
import java.util.Optional;

public interface CacheService {
    Optional<CurrencyRate> getRate(CurrencyCodePair codePair);
    void putRate(CurrencyRate rate);
    void putRates(Map<CurrencyCodePair, CurrencyRate> rates);
    void clearCache();
}
