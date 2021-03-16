package com.example.reactivepostgredemo.router;

import com.example.reactivepostgredemo.handler.SampleHandler;
import com.example.reactivepostgredemo.handler.TodoHandler;
import com.example.reactivepostgredemo.model.Todo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.transport.ServerTransport;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class SampleRouter {

    @Bean
    public RouterFunction<ServerResponse> route(SampleHandler handler){
        return RouterFunctions
                .route( GET("/functional/flux").and(accept(APPLICATION_JSON)), handler::findAll);
    }

    @Bean
    public RouterFunction<ServerResponse> root(SampleHandler handler){
        //RouterFunctions.route()가 제공하는 빌더를 사용.
        return RouterFunctions.route()
                .GET("/sample", accept(APPLICATION_JSON), handler::findAll)
                .GET("sample/{id}", accept(APPLICATION_JSON), handler::findById)
                .POST("/sample", accept(APPLICATION_JSON), handler::save)
                .PUT("/sample/{id}", accept(APPLICATION_JSON), handler::update)
                .DELETE("/sample/{id}", accept(APPLICATION_JSON), handler::delete)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> todoRoute(TodoHandler handler){
        //Nested Routes 라우터 펑션을 그룹핑 중복 코드를 줄임
        return RouterFunctions.route()
                .path("/functionaltodo", b1 -> b1
                        .nest(accept(APPLICATION_JSON), b2 -> b2
                                .GET("/", handler::findAll)
                                .GET("/{task_no}", handler::findById)
                                .POST("/", handler::save)
                                .PUT("/{task_no}", handler::update)
                                .DELETE("/{task_no}", handler::delete)
                        ))
                .build();
    }



}