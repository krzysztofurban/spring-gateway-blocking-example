package com.example.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class BlockingHeaderGatewayFilterFactory extends AbstractGatewayFilterFactory<BlockingHeaderGatewayFilterFactory.Config> {

    private final RestConnector restConnector;

    @Autowired
    public BlockingHeaderGatewayFilterFactory(RestConnector restConnector) {
        super(Config.class);
        this.restConnector = restConnector;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest.Builder headers = exchange.getRequest()
                    .mutate()
                    .headers(originalHeaders -> {
                        String statusCode = restConnector.blockingConnector();
                        originalHeaders.add("blocking-header", statusCode);
                    });

            return chain.filter(exchange.mutate().request(headers.build()).build());
        };
    }

    public static class Config {
    }
}