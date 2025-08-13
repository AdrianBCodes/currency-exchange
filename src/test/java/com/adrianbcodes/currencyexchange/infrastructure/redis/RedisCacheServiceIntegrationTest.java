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
        ExchangeRate exchangeRate = new ExchangeRate(BigDecimal.valueOf(4.1234));

        Instant before = Instant.now();
        cacheService.putRate(codePair, exchangeRate);
        Instant after = Instant.now();

        Optional<CurrencyRate> result = cacheService.getRate(codePair);
        assertTrue(result.isPresent());
        assertEquals(result.get().getRate(), exchangeRate);
        assertFalse(result.get().getDate().isBefore(before));
        assertFalse(result.get().getDate().isAfter(after));
    }

    @Test
    void putRates_storesAllRates() {
        CurrencyCodePair codePair = new CurrencyCodePair(new CurrencyCode("USD"), new CurrencyCode("PLN"));
        ExchangeRate exchangeRate = new ExchangeRate(BigDecimal.valueOf(4.1234));
        CurrencyCodePair codePair2 = new CurrencyCodePair(new CurrencyCode("EUR"), new CurrencyCode("PLN"));
        ExchangeRate exchangeRate2 = new ExchangeRate(BigDecimal.valueOf(4.4321));
        Map<CurrencyCodePair, ExchangeRate> mapToPut = new HashMap<>() {{
            put(codePair, exchangeRate);
            put(codePair2, exchangeRate2);
        }};

        cacheService.putRates(mapToPut);

        Optional<CurrencyRate> result = cacheService.getRate(codePair);
        Optional<CurrencyRate> result2 = cacheService.getRate(codePair2);
        assertTrue(result.isPresent());
        assertTrue(result2.isPresent());
        assertEquals(result.get().getRate(), exchangeRate);
        assertEquals(result2.get().getRate(), exchangeRate2);
    }
}