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

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class RedisCacheService implements CacheService {

    private final static String CACHE_KEY_DEFAULT = "default_currency_exchange_rates";
    private final static String CACHE_KEY = "currency_exchange_rates";

    private final RedisTemplate<String, CurrencyRateValueDto> redisTemplate;

    private final ObjectMapper objectMapper;

    RedisCacheService(@Qualifier("redisCurrencyRateTemplate") RedisTemplate<String, CurrencyRateValueDto> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<CurrencyRate> getRate(CurrencyCodePair codePair) {
        String cacheKey = codePair.to().code().equals("PLN") ? CACHE_KEY_DEFAULT : CACHE_KEY;
        Object value = redisTemplate.opsForHash().get(cacheKey, codePair.toKey());
        if(value == null)
            return Optional.empty();
        CurrencyRateValueDto dto = objectMapper.convertValue(value, CurrencyRateValueDto.class);
        CurrencyRate rate = new CurrencyRate(codePair, new ExchangeRate(dto.rate()), dto.date());
        return Optional.of(rate);
    }

    @Override
    public void putRate(CurrencyRate rate) {
        CurrencyRateValueDto dto = new CurrencyRateValueDto(rate.rate().value(), rate.date());
        redisTemplate.opsForHash().put(CACHE_KEY, rate.codePair().toKey(), dto);
    }

    @Override
    public void putRates(Map<CurrencyCodePair, CurrencyRate> rates) {
        Map<String, CurrencyRateValueDto> mapToPut = rates.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toKey(),
                        entry -> new CurrencyRateValueDto(entry.getValue().rate().value(), entry.getValue().date())
                ));
        redisTemplate.opsForHash().putAll(CACHE_KEY_DEFAULT, mapToPut);
    }

    @Override
    public void clearCache() {
        redisTemplate.delete(CACHE_KEY);
    }
}
