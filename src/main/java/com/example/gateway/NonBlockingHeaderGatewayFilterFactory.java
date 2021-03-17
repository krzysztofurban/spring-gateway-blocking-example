package com.example.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class NonBlockingHeaderGatewayFilterFactory extends AbstractGatewayFilterFactory<NonBlockingHeaderGatewayFilterFactory.Config> {

    final Logger logger = LoggerFactory.getLogger(NonBlockingHeaderGatewayFilterFactory.class);

    public NonBlockingHeaderGatewayFilterFactory() {
        super(Config.class);
    }

    @Autowired
    RestConnector restConnector;

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
        private String baseMessage;

        public Config() {
        }

        public Config(String baseMessage) {
            this.baseMessage = baseMessage;
        }

        public void setBaseMessage(String baseMessage) {
            this.baseMessage = baseMessage;
        }

        public String getBaseMessage() {
            return baseMessage;
        }
    }
}