package com.infnet.crypto_quotation.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class QuotationService {

    private final WebClient webClient;

    public QuotationService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.coingecko.com/api/v3").build();
    }

    public Mono<String> getPreco(String moedaId) {
        // Ex: moedaId = "bitcoin"
        return this.webClient.get()
                .uri("/simple/price?ids={id}&vs_currencies=usd", moedaId)
                .retrieve()
                .bodyToMono(String.class);
    }
}