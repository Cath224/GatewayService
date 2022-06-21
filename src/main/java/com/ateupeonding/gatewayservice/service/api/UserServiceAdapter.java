package com.ateupeonding.gatewayservice.service.api;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface UserServiceAdapter {

    Mono<ResponseEntity<Void>> verify(String token);

}
