package com.example.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RestConnector {

    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final String baseUrl;

    @Autowired
    RestConnector(RestTemplateBuilder restTemplate, WebClient.Builder webClientBuilder, @Value("${test.delay}") Integer delay) {
        this.restTemplate = restTemplate.build();
        this.webClient = webClientBuilder.build();
        this.baseUrl = "https://httpbin.org/delay/" + delay;
    }

    public Mono<String> nonBlockingConnector() {
        Mono<String> result = WebClient.create(baseUrl)
                .get()
                .retrieve()
                .bodyToMono(String.class);

        return result;
    }

    public String blockingConnector() {
        ResponseEntity<String> result = restTemplate.getForEntity(baseUrl, String.class);
        return result.getStatusCode().toString();
    }
}
