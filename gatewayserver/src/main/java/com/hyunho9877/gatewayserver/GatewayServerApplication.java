package com.hyunho9877.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@EnableEurekaClient
@SpringBootApplication

public class GatewayServerApplication {

    public GatewayServerApplication(TokenRelayGatewayFilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }


    private final TokenRelayGatewayFilterFactory filterFactory;

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/eazybank/accounts/**")
                        .filters(f -> f.filters(filterFactory.apply())
                                .rewritePath("/eazybank/accounts/(?<segment>.*)", "/${segment}")
                                .removeRequestHeader("Cookies"))
                        .uri("lb://ACCOUNTS")).
                route(p -> p
                        .path("/eazybank/loans/**")
                        .filters(f -> f.filters(filterFactory.apply())
                                .rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}")
                                .removeRequestHeader("Cookies"))
                        .uri("lb://LOANS")).
                route(p -> p
                        .path("/eazybank/cards/**")
                        .filters(f -> f.filters(filterFactory.apply())
                                .rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
                                .removeRequestHeader("Cookies"))
                        .uri("lb://CARDS")).build();
    }
}
