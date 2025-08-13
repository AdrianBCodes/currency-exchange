package com.adrianbcodes.currencyexchange.infrastructure.redis;

import com.adrianbcodes.currencyexchange.application.port.out.CacheService;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCodePair;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyRate;
import com.adrianbcodes.currencyexchange.domain.currency.ExchangeRate;
import com.adrianbcodes.currencyexchange.infrastructure.redis.dto.CurrencyRateValueDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class RedisCacheService implements CacheService {

    private final static String CACHE_KEY = "currency_exchange_rates";

    private final RedisTemplate<String, CurrencyRateValueDto> redisTemplate;

    private final ObjectMapper objectMapper;

    RedisCacheService(@Qualifier("redisCurrencyRateTemplate") RedisTemplate<String, CurrencyRateValueDto> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<CurrencyRate> getRate(CurrencyCodePair codePair) {
        Object value = redisTemplate.opsForHash().get(CACHE_KEY, codePair.toKey());
        if(value == null)
            return Optional.empty();
        CurrencyRateValueDto dto = objectMapper.convertValue(value, CurrencyRateValueDto.class);
        CurrencyRate rate = new CurrencyRate(codePair.from(), codePair.to(), new ExchangeRate(dto.rate()), dto.date());
        return Optional.of(rate);
    }

    @Override
    public void putRate(CurrencyCodePair codePair, ExchangeRate rate) {
        CurrencyRateValueDto dto = new CurrencyRateValueDto(rate.value(), Instant.now());
        redisTemplate.opsForHash().put(CACHE_KEY, codePair.toKey(), dto);
    }

    @Override
    public void putRates(Map<CurrencyCodePair, ExchangeRate> rates) {
        Map<String, CurrencyRateValueDto> mapToPut = rates.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toKey(),
                        entry -> new CurrencyRateValueDto(entry.getValue().value(), Instant.now())
                ));
        redisTemplate.opsForHash().putAll(CACHE_KEY, mapToPut);
    }
}
