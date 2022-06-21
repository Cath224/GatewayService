package com.ateupeonding.gatewayservice.configuration;

import com.ateupeonding.gatewayservice.service.api.UserServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Order(1)
@Component
public class JwtFilter implements GlobalFilter {

    @Autowired
    private UserServiceAdapter userServiceAdapter;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        RequestPath path = exchange.getRequest().getPath();
        PathContainer pathContainer = path.pathWithinApplication();
        List<PathContainer.Element> elements = pathContainer.elements();
        if (elements.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        PathContainer.Element element = elements.get(1);
        if (element != null && ("user-service".equals(element.value()) ||
                "content-service".equals(element.value())
                        && HttpMethod.GET.equals(exchange.getRequest().getMethod()))) {
            return chain.filter(exchange);
        }

        List<String> authHeaders =
                exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (authHeaders == null || authHeaders.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String header = authHeaders.get(0);

        return userServiceAdapter.verify(header)
                .flatMap((el) -> chain.filter(exchange));
    }


}
