package com.example.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RestConnectorTest {

    @Autowired
    RestConnector restConnector;

    @Test
    void testNonBlockingConnector() {
        String result = restConnector.nonBlockingConnector().block();
        assertThat(result).isEqualTo("200");
    }

    @Test
    void testBlockingConnector() {
        String result = restConnector.blockingConnector();
        assertThat(result).isEqualTo("200 OK");
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme