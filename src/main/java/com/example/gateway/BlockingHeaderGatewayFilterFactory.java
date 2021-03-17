package com.example.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class BlockingHeaderGatewayFilterFactory extends AbstractGatewayFilterFactory<BlockingHeaderGatewayFilterFactory.Config> {

    final Logger logger = LoggerFactory.getLogger(BlockingHeaderGatewayFilterFactory.class);

    @Autowired
    RestConnector restConnector;

    public BlockingHeaderGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            logger.info("Config base message {}", config.getBaseMessage() );

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