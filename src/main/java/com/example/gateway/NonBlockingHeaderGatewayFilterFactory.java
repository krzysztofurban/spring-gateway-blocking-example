package com.example.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class NonBlockingHeaderGatewayFilterFactory extends AbstractGatewayFilterFactory<NonBlockingHeaderGatewayFilterFactory.Config> {

    private final RestConnector restConnector;

    @Autowired
    public NonBlockingHeaderGatewayFilterFactory(RestConnector restConnector) {
        super(Config.class);
        this.restConnector = restConnector;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) ->
                restConnector.nonBlockingConnector().map(
                        response -> {
                            exchange.getRequest()
                                    .mutate()
                                    .headers(originalHeaders -> originalHeaders.add("non-blocking-header", response.substring(0, 20)))
                                    .build();

                            return exchange;
                        }).flatMap(chain::filter);
    }

    public static class Config {
    }
}