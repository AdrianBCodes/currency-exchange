package com.adrianbcodes.currencyexchange.domain.currency;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateTest {
    
    @Test
    void exchangeRateCreation() {
        ExchangeRate exchangeRate = new ExchangeRate(BigDecimal.valueOf(1.4351));
        assertEquals(BigDecimal.valueOf(1.4351), exchangeRate.rate());
    }

    @Test
    void exchangeRateCreation_ReducesScaleTo4AndRoundsUp() {
        ExchangeRate exchangeRate = new ExchangeRate(BigDecimal.valueOf(1.43511));
        assertEquals(BigDecimal.valueOf(1.4352), exchangeRate.rate());
    }

    @Test
    void exchangeRateCreation_IncreasesScaleTo4() {
        ExchangeRate exchangeRate = new ExchangeRate(BigDecimal.valueOf(1.43));
        assertEquals("1.4300", exchangeRate.rate().toString());
    }

    @Test
    void exchangeRateCreation_NullRate_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ExchangeRate(null));
    }

    @Test
    void exchangeRateCreation_LessOrEqualZero_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ExchangeRate(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> new ExchangeRate(BigDecimal.valueOf(-1)));
    }
}