package com.example.reactivepostgredemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFlux
public class exampleWebFilter implements WebFilter {
//webfilter 쓰는이유?
//    1. mvc의 interceptor 대신 쓴다(전처리)

//언제쓰는지?
//    1. corswebfiler
//    2. 인증, 인가
//    3. bad request 생성필요힐때(헤더 및 쿠키 검증)
//    4. response 세션 및 쿠키 처리

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange,
                             WebFilterChain webFilterChain) {

        serverWebExchange.getResponse()
                .getHeaders()
                .add("web-filter", "web-filter-test");

        return webFilterChain.filter(serverWebExchange);
    }
}

