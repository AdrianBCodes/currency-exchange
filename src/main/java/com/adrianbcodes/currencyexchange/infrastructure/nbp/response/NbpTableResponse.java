package com.adrianbcodes.currencyexchange.infrastructure.nbp.response;

import java.util.List;

public record NbpTableResponse(String table, String no, String effectiveDate,List<NbpCurrencyRate> rates) {
}
