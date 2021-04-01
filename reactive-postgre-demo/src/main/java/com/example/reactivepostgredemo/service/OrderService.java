package com.example.reactivepostgredemo.service;

import com.example.reactivepostgredemo.model.OmOd;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface OrderService {

    Flux<OmOd> getOrderList();

    Mono<OmOd> findByOdNo(String odNo);

    Mono<String> makeOdNo();

    Mono<OmOd> getWithDataOrderItem(String odNo);

    Mono<OmOd> createOrder(OmOd bodyToMono);
}
