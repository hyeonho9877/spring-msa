package com.hyunho9877.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable()
                .authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers("/eazybank/accounts/**").authenticated()
                                .pathMatchers("/eazybank/cards/**").authenticated()
                                .pathMatchers("/eazybank/loans/**").permitAll())
                .oauth2Login(Customizer.withDefaults());
        return http.build();
    }
}
