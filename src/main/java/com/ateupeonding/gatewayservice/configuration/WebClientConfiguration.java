package com.ateupeonding.gatewayservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Value("${spring.cloud.gateway.routes[3].uri}")
    private String userServiceUrl;

    @Bean("userServiceWebClient")
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(userServiceUrl)
                .build();
    }

}
