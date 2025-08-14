package com.adrianbcodes.currencyexchange.infrastructure.nbp;

import com.adrianbcodes.currencyexchange.application.port.out.CacheService;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCode;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCodePair;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyRate;
import com.adrianbcodes.currencyexchange.domain.currency.ExchangeRate;
import com.adrianbcodes.currencyexchange.infrastructure.nbp.response.NbpTableResponse;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NbpRatesScheduler {
    private final NbpApiClient nbpApiClient;
    private final CacheService cacheService;
    private final RedissonClient redissonClient;

    public NbpRatesScheduler(NbpApiClient nbpApiClient, CacheService cacheService, RedissonClient redissonClient) {
        this.nbpApiClient = nbpApiClient;
        this.cacheService = cacheService;
        this.redissonClient = redissonClient;
    }

    // https://nbp.pl/statystyka-i-sprawozdawczosc/kursy/informacja-o-terminach-publikacji-kursow-walut

    // Table A - Nbp updates every working day between 11:45 - 12:15 CEST
    @Scheduled(cron = "0 20 12 * * MON-FRI", zone = "Europe/Warsaw")
    public void updateTableA() {
        RLock lock = redissonClient.getLock("update-table-A-lock");
        if(lock.tryLock()){
            try {
                updateRates("A");
            } finally {
                lock.unlock();
            }
        }
    }

    // Table B - Nbp updates every wednesday between 11:45 - 12:15 CEST
    // TODO: consider non-working days - fetch day before
    @Scheduled(cron = "0 20 12 * * WED", zone = "Europe/Warsaw")
    public void updateTableB() {
        RLock lock = redissonClient.getLock("update-table-B-lock");
        if(lock.tryLock()){
            try {
                updateRates("B");
            } finally {
                lock.unlock();
            }
        }
    }

    void updateRates(String tableCode) {
        NbpTableResponse tableResponse = nbpApiClient.fetchCurrentTable(tableCode);

        Map<CurrencyCodePair, CurrencyRate> ratesToCache = tableResponse.rates().stream()
                .collect(Collectors.toMap(
                        rate -> new CurrencyCodePair(new CurrencyCode(rate.code()), new CurrencyCode("PLN")),
                        rate -> new CurrencyRate(
                                new CurrencyCodePair(new CurrencyCode(rate.code()), new CurrencyCode("PLN")),
                                new ExchangeRate(rate.mid()),
                                Instant.now()
                        )
                ));


        // TODO: move to initial redis setup
        CurrencyCodePair codePairPLN = new CurrencyCodePair(new CurrencyCode("PLN"), new CurrencyCode("PLN"));
        ratesToCache.put(codePairPLN,
                new CurrencyRate(
                        codePairPLN,
                        new ExchangeRate(BigDecimal.ONE),
                        Instant.now()
                ));

        cacheService.clearCache();
        cacheService.putRates(ratesToCache);
    }
}
