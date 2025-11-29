package com.infnet.crypto_quotation.controller;

import com.infnet.crypto_quotation.service.QuotationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/quotation")
public class QuotationController {

    private final QuotationService service;

    public QuotationController(QuotationService service) {
        this.service = service;
    }

    @GetMapping("/{moeda}")
    public Mono<String> getCotacao(@PathVariable String moeda) {
        return service.getPreco(moeda);
    }
}