package com.adrianbcodes.currencyexchange.domain.currency;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyCodeTest {

    @Test
    void currencyCodeCreation() {
        CurrencyCode currencyCode = new CurrencyCode("USD");
        assertEquals("USD", currencyCode.code());
    }

    @Test
    void currencyCodeCreation_NullCode_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CurrencyCode(null));
    }

    @Test
    void currencyCodeCreation_convertsLowerCharactersToUpper() {
        CurrencyCode currencyCode = new CurrencyCode("usD");
        assertEquals("USD", currencyCode.code());
    }

    @Test
    void currencyCodeCreation_LengthOtherThanThree_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new CurrencyCode("d"));
        assertThrows(IllegalArgumentException.class, () -> new CurrencyCode("dddd"));
    }

    @Test
    void currencyCodeCreation_CharactersOtherThanLetters_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new CurrencyCode("1"));
    }

}