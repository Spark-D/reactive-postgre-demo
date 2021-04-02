package com.example.reactivepostgredemo.router;

import com.example.reactivepostgredemo.handler.OrderHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class OrderRouter {

    @Bean
    public RouterFunction<ServerResponse> orderRoute(OrderHandler handler){
        return RouterFunctions.route()
                .GET("/order", accept(APPLICATION_JSON), handler::getOrderList)
                .GET("/order/{odNo}", accept(APPLICATION_JSON), handler::getOrderItemByOdNo)
                .POST("/order", accept(APPLICATION_JSON), handler::createOrder)
                .GET("/client/projection", accept(APPLICATION_JSON), handler::orderJoinList)
                .GET("/client/bifunction", accept(APPLICATION_JSON), handler::orderbifuncList)
                .GET("/client/test", accept(APPLICATION_JSON), handler::testList)
                .POST("/order/cancel", accept(APPLICATION_JSON), handler::orderCancel)
                .build();
    }
}
