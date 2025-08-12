package com.adrianbcodes.currencyexchange.infrastructure.nbp;

import com.adrianbcodes.currencyexchange.infrastructure.nbp.response.NbpTableResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class NbpWebClient {
    private final WebClient webClient;

    public NbpWebClient(@Qualifier("nbpWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public NbpTableResponse fetchCurrentTable(String tableCode) {
        String tableCodeUpper = tableCode.toUpperCase();

        if(!List.of("A", "B", "C").contains(tableCodeUpper))
            throw new IllegalArgumentException("Invalid Nbp table code:" + tableCode);

        NbpTableResponse[] response = webClient.get()
                .uri("/exchangerates/tables/" + tableCodeUpper)
                .retrieve()
                .bodyToMono(NbpTableResponse[].class)
                .block();


        if(response == null || response.length == 0)
            throw new IllegalStateException("No data found in Nbp API fetch table" + tableCode);

        return response[0];
    }
}
