package com.adrianbcodes.currencyexchange.infrastructure.nbp;

import com.adrianbcodes.currencyexchange.application.port.out.CacheService;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCode;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCodePair;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyRate;
import com.adrianbcodes.currencyexchange.domain.currency.ExchangeRate;
import com.adrianbcodes.currencyexchange.infrastructure.nbp.response.NbpCurrencyRate;
import com.adrianbcodes.currencyexchange.infrastructure.nbp.response.NbpTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class NbpRatesSchedulerTest {

    private NbpRatesScheduler nbpRatesScheduler;
    private NbpApiClient nbpApiClient;
    private CacheService cacheService;
    private RedissonClient redissonClient;
    private RLock lock;

    // TODO: move to separate Examples file
    private final NbpTableResponse tableAResponse = new NbpTableResponse(
            "A",
            "1",
            "2099-12-12",
            List.of(new NbpCurrencyRate("Dolar", "USD", BigDecimal.valueOf(4.1234))));

    private final NbpTableResponse TableBResponse = new NbpTableResponse(
            "B",
            "1",
            "2099-12-12",
            List.of(new NbpCurrencyRate("afgani (Afganistan)", "AFN", BigDecimal.valueOf(0.5329))));

    @BeforeEach
    void setUp() {
        nbpApiClient = mock(NbpApiClient.class);
        cacheService = mock(CacheService.class);
        redissonClient = mock(RedissonClient.class);
        lock = mock(RLock.class);

        when(nbpApiClient.fetchCurrentTable("A"))
                .thenReturn(tableAResponse);

        when(nbpApiClient.fetchCurrentTable("B"))
                .thenReturn(TableBResponse);
    }

    @Test
    void updateTableA_LockFree_ShouldCallUpdateRatesAndUnlock() {
        // given
        nbpRatesScheduler = spy(new NbpRatesScheduler(nbpApiClient, cacheService, redissonClient));
        when(redissonClient.getLock("update-table-A-lock")).thenReturn(lock);
        when(lock.tryLock()).thenReturn(true);
        doNothing().when(nbpRatesScheduler).updateRates("A");

        // when
        nbpRatesScheduler.updateTableA();

        // then
        verify(nbpRatesScheduler).updateRates("A");
        verify(lock).unlock();
    }

    @Test
    void updateTableB_LockFree_ShouldCallUpdateRatesAndUnlock() {
        // given
        nbpRatesScheduler = spy(new NbpRatesScheduler(nbpApiClient, cacheService, redissonClient));
        when(redissonClient.getLock("update-table-B-lock")).thenReturn(lock);
        when(lock.tryLock()).thenReturn(true);
        doNothing().when(nbpRatesScheduler).updateRates("B");

        // when
        nbpRatesScheduler.updateTableB();

        // then
        verify(nbpRatesScheduler).updateRates("B");
        verify(lock).unlock();
    }

    @Test
    void updateTableA_LockBlocked_ShouldNotCallUpdateRates() {
        // given
        nbpRatesScheduler = spy(new NbpRatesScheduler(nbpApiClient, cacheService, redissonClient));
        when(redissonClient.getLock("update-table-A-lock")).thenReturn(lock);
        when(lock.tryLock()).thenReturn(false);

        // when
        nbpRatesScheduler.updateTableA();

        // then
        verify(nbpRatesScheduler, never()).updateRates("A");
        verify(lock, never()).unlock();
    }

    @Test
    void updateTableB_LockBlocked_ShouldNotCallUpdateRates() {
        // given
        nbpRatesScheduler = spy(new NbpRatesScheduler(nbpApiClient, cacheService, redissonClient));
        when(redissonClient.getLock("update-table-B-lock")).thenReturn(lock);
        when(lock.tryLock()).thenReturn(false);

        // when
        nbpRatesScheduler.updateTableB();

        // then
        verify(nbpRatesScheduler, never()).updateRates("B");
        verify(lock, never()).unlock();
    }

    @Test
    void updateRates() {
        // given
        nbpRatesScheduler = new NbpRatesScheduler(nbpApiClient, cacheService, redissonClient);

        // when
        nbpRatesScheduler.updateRates("A");

        // then
        verify(cacheService).clearCache();
        verify(nbpApiClient).fetchCurrentTable("A");
        //TODO: change to actual expected value
        verify(cacheService).putRates(anyMap());
    }
}