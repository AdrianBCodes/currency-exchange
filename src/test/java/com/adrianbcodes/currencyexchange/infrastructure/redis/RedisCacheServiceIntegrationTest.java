package com.adrianbcodes.currencyexchange.infrastructure.redis;

import com.adrianbcodes.currencyexchange.application.port.out.CacheService;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCode;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCodePair;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyRate;
import com.adrianbcodes.currencyexchange.domain.currency.ExchangeRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
class RedisCacheServiceIntegrationTest {

    private final CacheService cacheService;

    public RedisCacheServiceIntegrationTest(@Qualifier("redisCacheService") CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Test
    void getRateAndPutRate_storesValidRateAndTimeStamp() {
        CurrencyCodePair codePair = new CurrencyCodePair(new CurrencyCode("USD"), new CurrencyCode("PLN"));
        CurrencyRate currencyRate = new CurrencyRate(codePair, new ExchangeRate(BigDecimal.valueOf(4.1234)), Instant.now());

        Instant before = Instant.now();
        cacheService.putRate(currencyRate);
        Instant after = Instant.now();

        Optional<CurrencyRate> result = cacheService.getRate(codePair);
        assertTrue(result.isPresent());
        assertEquals(result.get(), currencyRate);
        assertFalse(result.get().date().isBefore(before));
        assertFalse(result.get().date().isAfter(after));
    }

    @Test
    void putRates_storesAllRates() {
        CurrencyCodePair codePair = new CurrencyCodePair(new CurrencyCode("USD"), new CurrencyCode("PLN"));
        CurrencyRate currencyRate = new CurrencyRate(codePair, new ExchangeRate(BigDecimal.valueOf(4.1234)), Instant.now());
        CurrencyCodePair codePair2 = new CurrencyCodePair(new CurrencyCode("EUR"), new CurrencyCode("PLN"));
        CurrencyRate currencyRate2 = new CurrencyRate(codePair2, new ExchangeRate(BigDecimal.valueOf(4.4321)), Instant.now());
        Map<CurrencyCodePair, CurrencyRate> mapToPut = new HashMap<>() {{
            put(codePair, currencyRate);
            put(codePair2, currencyRate2);
        }};

        cacheService.putRates(mapToPut);

        Optional<CurrencyRate> result = cacheService.getRate(codePair);
        Optional<CurrencyRate> result2 = cacheService.getRate(codePair2);
        assertTrue(result.isPresent());
        assertTrue(result2.isPresent());
        assertEquals(result.get(), currencyRate);
        assertEquals(result2.get(), currencyRate2);
    }
}