package com.example.reactivepostgredemo.service;

import com.example.reactivepostgredemo.model.TodoDto;
import com.example.reactivepostgredemo.model.TodoProjection;
import com.example.reactivepostgredemo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TodoServiceImpl implements TodoService{
    
    @Autowired
    TodoRepository todoRepository;
    
    @Override
    public Flux<TodoProjection> getAllByProjection() {
        return todoRepository.findAllBy();
    }

    @Override
    public Flux<TodoDto> getAllByDto() {
        return todoRepository.getAllBy();
    }
}
