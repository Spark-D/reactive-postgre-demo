package com.example.reactivepostgredemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.awt.*;

@Configuration
@EnableWebFlux
public class exampleWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange,
                             WebFilterChain webFilterChain) {

        serverWebExchange.getResponse()
                .getHeaders()
                .add("web-filter", "web-filter-test");

        return webFilterChain.filter(serverWebExchange);
    }
}

