package com.adrianbcodes.currencyexchange.application.service;

import com.adrianbcodes.currencyexchange.application.port.out.CacheService;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCode;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCodePair;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyRate;
import com.adrianbcodes.currencyexchange.domain.currency.ExchangeRate;
import com.adrianbcodes.currencyexchange.domain.exception.RateNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class CurrencyExchangeService {
    private final CacheService cacheService;

    public CurrencyExchangeService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public CurrencyRate getRate(CurrencyCodePair codePair) {
        if(codePair == null)
            throw new IllegalArgumentException("CurrencyCodePair cannot be null");

        Optional<CurrencyRate> cachedRate = cacheService.getRate(codePair);
        if(cachedRate.isPresent())
            return cachedRate.get();

        CurrencyCodePair fromPlnPair = new CurrencyCodePair(codePair.from(), new CurrencyCode("PLN"));
        CurrencyCodePair toPlnPair = new CurrencyCodePair(codePair.to(), new CurrencyCode("PLN"));

        CurrencyRate fromPlnRate = cacheService.getRate(fromPlnPair)
                .orElseThrow(() -> new RateNotFoundException("Missing intermediate rate: " + fromPlnPair));
        CurrencyRate toPlnRate = cacheService.getRate(toPlnPair)
                .orElseThrow(() -> new RateNotFoundException("Missing intermediate rate: " + toPlnPair));

        ExchangeRate finalRate = fromPlnRate.rate().divide(toPlnRate.rate());
        CurrencyRate result = new CurrencyRate(codePair, finalRate, Instant.now());
        cacheService.putRate(result);
        return result;
    }
}
