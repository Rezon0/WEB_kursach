package com.example.russionTest.domain;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class HtmlDownloader {

    private final WebClient webClient;

    public HtmlDownloader() {
        this.webClient = WebClient.create();
    }

    public Mono<String> downloadHtml(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);
    }
}
