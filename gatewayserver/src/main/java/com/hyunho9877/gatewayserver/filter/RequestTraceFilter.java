package com.hyunho9877.gatewayserver.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);
    private final FilterUtility filterUtility;

    public RequestTraceFilter(FilterUtility filterUtility) {
        this.filterUtility = filterUtility;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        if (isCorrelationIdPresent(headers)) {
            logger.debug("EazyBank-correlation-id found in tracing filter : {}.", filterUtility.getCorrelationId(headers));
        } else {
            String correlationId = generateCorrelationId();
            exchange = filterUtility.setCorrelationId(exchange, correlationId);
            logger.debug("EazyBank-correlation-id generated in tracing filter : {}.", correlationId);
        }
        return chain.filter(exchange);
    }

    private boolean isCorrelationIdPresent(HttpHeaders headers) {
        if (filterUtility.getCorrelationId(headers) != null) return true;
        else return false;
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
