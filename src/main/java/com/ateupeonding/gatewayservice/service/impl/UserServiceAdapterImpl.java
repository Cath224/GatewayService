package com.ateupeonding.gatewayservice.service.impl;

import com.ateupeonding.gatewayservice.service.api.UserServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class UserServiceAdapterImpl implements UserServiceAdapter {

    private static final String VERIFICATION_URL = "/user-service/api/v1/auth/verify";

    @Autowired
    @Qualifier("userServiceWebClient")
    private WebClient webClient;

    @Override
    public Mono<ResponseEntity<Void>> verify(String token) {
        WebClient.ResponseSpec result = webClient.post()
                .uri(VERIFICATION_URL)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve();
        return result.onStatus((el) -> el.equals(HttpStatus.UNAUTHORIZED), (el) -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)))
                .toBodilessEntity();
    }
}
