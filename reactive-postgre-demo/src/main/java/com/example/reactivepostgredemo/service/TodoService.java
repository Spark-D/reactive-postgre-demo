package com.example.reactivepostgredemo.service;

import com.example.reactivepostgredemo.model.TodoDto;
import com.example.reactivepostgredemo.model.TodoProjection;
import reactor.core.publisher.Flux;


public interface TodoService {
    Flux<TodoProjection> getAllByProjection();

    Flux<TodoDto> getAllByDto();
}
