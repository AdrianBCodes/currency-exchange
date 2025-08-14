package com.adrianbcodes.currencyexchange.infrastructure.nbp;

import com.adrianbcodes.currencyexchange.infrastructure.nbp.response.NbpTableResponse;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.web.reactive.function.client.WebClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class NbpApiClientTest {

    @Value("${wiremock.server.port}")
    private int wireMockPort;

    private NbpApiClient nbpApiClient;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @BeforeEach
    void setUp() {
        WebClient webClient = webClientBuilder
                .baseUrl("http://localhost:" + wireMockPort)
                .build();
        nbpApiClient = new NbpApiClient(webClient);
        WireMock.configureFor("localhost", wireMockPort);
    }

    @Test
    void fetchCurrentTable() {
        // given
        String mockResponse = """
                [
                  {
                    "table": "A",
                    "no": "156/A/NBP/2025",
                    "effectiveDate": "2025-08-13",
                    "rates": [
                      {
                        "currency": "bat (Tajlandia)",
                        "code": "THB",
                        "mid": 0.1126
                      }
                    ]
                  }
                ]
                """;

        stubFor(get(urlEqualTo("/exchangerates/tables/A"))
                .willReturn(okJson(mockResponse)));

        // when
        NbpTableResponse response = nbpApiClient.fetchCurrentTable("A");

        // then
        assertNotNull(response);
        assertEquals("A", response.table());
        assertEquals(1, response.rates().size());
    }

    @Test
    void fetchCurrentTable_invalidTableCode_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> nbpApiClient.fetchCurrentTable("Z"));
    }

    @Test
    void fetchCurrentTable_NoDataReturned_ThrowsIllegalStateException() {
        stubFor(get(urlEqualTo("/exchangerates/tables/A"))
                .willReturn(okJson("[]")));

        assertThrows(IllegalStateException.class, () -> nbpApiClient.fetchCurrentTable("A"));
    }
}