package com.adrianbcodes.currencyexchange.application.service;

import com.adrianbcodes.currencyexchange.application.port.out.CacheService;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCode;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCodePair;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyRate;
import com.adrianbcodes.currencyexchange.domain.currency.ExchangeRate;
import com.adrianbcodes.currencyexchange.domain.exception.RateNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyExchangeServiceTest {

    private CacheService cacheService;
    private CurrencyExchangeService currencyExchangeService;

    @BeforeEach
    void setUp() {
        cacheService = mock(CacheService.class);
        currencyExchangeService = new CurrencyExchangeService(cacheService);
    }

    @Test
    void getRate_cachedRate() {
        // given
        CurrencyCodePair codePair = new CurrencyCodePair(new CurrencyCode("USD"), new CurrencyCode("PLN"));
        CurrencyRate cachedRate = new CurrencyRate(codePair, new ExchangeRate(BigDecimal.valueOf(4.1234)), Instant.now());
        when(cacheService.getRate(codePair)).thenReturn(Optional.of(cachedRate));

        // when
        var result = currencyExchangeService.getRate(codePair);

        // then
        assertEquals(cachedRate, result);
        verify(cacheService, times(1)).getRate(codePair);
    }

    @Test
    void getRate_NullCodePair_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                currencyExchangeService.getRate(null));
    }

    @Test
    void getRate_RateNotInCache_ShouldCalculateAndCacheRate() {
        // given
        CurrencyCodePair codePair = new CurrencyCodePair(new CurrencyCode("USD"), new CurrencyCode("EUR"));
        CurrencyRate expectedResult = new CurrencyRate(codePair, new ExchangeRate(BigDecimal.valueOf(5)), Instant.now());
        when(cacheService.getRate(codePair)).thenReturn(Optional.empty());

        CurrencyCodePair intermediateCodePair = new CurrencyCodePair(new CurrencyCode("USD"), new CurrencyCode("PLN"));
        CurrencyRate cachedIntermediateRate = new CurrencyRate(intermediateCodePair, new ExchangeRate(BigDecimal.valueOf(20)), Instant.now());
        when(cacheService.getRate(intermediateCodePair)).thenReturn(Optional.of(cachedIntermediateRate));

        CurrencyCodePair intermediateCodePair2 = new CurrencyCodePair(new CurrencyCode("EUR"), new CurrencyCode("PLN"));
        CurrencyRate cachedIntermediateRate2 = new CurrencyRate(intermediateCodePair2, new ExchangeRate(BigDecimal.valueOf(4)), Instant.now());
        when(cacheService.getRate(intermediateCodePair2)).thenReturn(Optional.of(cachedIntermediateRate2));

        // when
        var result = currencyExchangeService.getRate(codePair);

        // then
        assertEquals(expectedResult.rate(), result.rate());
        assertEquals(expectedResult.codePair().from(), result.codePair().from());
        assertEquals(expectedResult.codePair().to(), result.codePair().to());
        verify(cacheService, times(1)).getRate(intermediateCodePair);
        verify(cacheService, times(1)).getRate(intermediateCodePair2);
        verify(cacheService, times(1)).putRate(result);
    }

    @Test
    void getRate_IntermediateRateNotInCache_ShouldThrowRateNotFoundException() {
        // given
        CurrencyCodePair codePair = new CurrencyCodePair(new CurrencyCode("USD"), new CurrencyCode("EUR"));
        when(cacheService.getRate(codePair)).thenReturn(Optional.empty());

        CurrencyCodePair intermediateCodePair = new CurrencyCodePair(new CurrencyCode("USD"), new CurrencyCode("PLN"));
        when(cacheService.getRate(intermediateCodePair)).thenReturn(Optional.empty());

        CurrencyCodePair intermediateCodePair2 = new CurrencyCodePair(new CurrencyCode("EUR"), new CurrencyCode("PLN"));
        CurrencyRate cachedIntermediateRate2 = new CurrencyRate(intermediateCodePair2, new ExchangeRate(BigDecimal.valueOf(4)), Instant.now());
        when(cacheService.getRate(intermediateCodePair2)).thenReturn(Optional.of(cachedIntermediateRate2));

        // when then
        assertThrows(RateNotFoundException.class, () -> currencyExchangeService.getRate(codePair));
        verify(cacheService, times(1)).getRate(intermediateCodePair);
        verify(cacheService, never()).putRate(any());
    }

    @Test
    void getRate_IntermediateRate2NotInCache_ShouldThrowRateNotFoundException() {
        // given
        CurrencyCodePair codePair = new CurrencyCodePair(new CurrencyCode("USD"), new CurrencyCode("EUR"));
        when(cacheService.getRate(codePair)).thenReturn(Optional.empty());

        CurrencyCodePair intermediateCodePair = new CurrencyCodePair(new CurrencyCode("USD"), new CurrencyCode("PLN"));
        CurrencyRate cachedIntermediateRate = new CurrencyRate(intermediateCodePair, new ExchangeRate(BigDecimal.valueOf(20)), Instant.now());
        when(cacheService.getRate(intermediateCodePair)).thenReturn(Optional.of(cachedIntermediateRate));

        CurrencyCodePair intermediateCodePair2 = new CurrencyCodePair(new CurrencyCode("EUR"), new CurrencyCode("PLN"));
        when(cacheService.getRate(intermediateCodePair2)).thenReturn(Optional.empty());

        // when then
        assertThrows(RateNotFoundException.class, () -> currencyExchangeService.getRate(codePair));
        verify(cacheService, times(1)).getRate(intermediateCodePair);
        verify(cacheService, times(1)).getRate(intermediateCodePair2);
        verify(cacheService, never()).putRate(any());
    }

}