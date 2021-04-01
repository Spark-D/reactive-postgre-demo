package com.example.reactivepostgredemo.handler;

import com.example.reactivepostgredemo.model.OmOd;
import com.example.reactivepostgredemo.repository.OrderDetailRepository;
import com.example.reactivepostgredemo.repository.OrderRepository;
import com.example.reactivepostgredemo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class OrderHandler {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    /**
     * 주문기본 전체 조회
     * */
    public Mono<ServerResponse> getOrderList(ServerRequest serverRequest) {
        Flux<OmOd> omOdFlux = orderService.getOrderList();

        return ServerResponse.ok().body(omOdFlux, OmOd.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * 주문 (base)단건 조회
     * */
    public Mono<ServerResponse> getOrderItemByOdNo(ServerRequest serverRequest) {
        String odNo = serverRequest.pathVariable("odNo");
        Mono<OmOd> omOdMono = orderService.getWithDataOrderItem(odNo);

        return ServerResponse.ok().body(omOdMono, OmOd.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * 주문 base 생성
     * */
    public Mono<ServerResponse> createOrder(ServerRequest serverRequest) {
        Mono<OmOd> omOdMono =serverRequest.bodyToMono(OmOd.class)
                .flatMap(orderService::createOrder);

        return ServerResponse.ok().body(omOdMono, OmOd.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
