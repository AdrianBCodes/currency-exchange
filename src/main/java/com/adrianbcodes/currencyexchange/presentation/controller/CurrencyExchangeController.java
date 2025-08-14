package com.adrianbcodes.currencyexchange.presentation.controller;

import com.adrianbcodes.currencyexchange.application.service.CurrencyExchangeService;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCode;
import com.adrianbcodes.currencyexchange.domain.currency.CurrencyCodePair;
import com.adrianbcodes.currencyexchange.presentation.controller.dto.CurrencyRateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/currency-exchange")
public class CurrencyExchangeController {
    private final CurrencyExchangeService currencyExchangeService;

    public CurrencyExchangeController(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }

    @GetMapping()
    public ResponseEntity<CurrencyRateDto> getRate(@RequestParam String from, @RequestParam String to) {
        CurrencyCodePair codePair = new CurrencyCodePair(new CurrencyCode(from), new CurrencyCode(to));
        return ResponseEntity.ok(CurrencyRateDto.fromDomain(currencyExchangeService.getRate(codePair)));
    }

}
