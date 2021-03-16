package com.example.reactivepostgredemo.controller;

import com.example.reactivepostgredemo.model.Customer;
import com.example.reactivepostgredemo.model.Todo;
import com.example.reactivepostgredemo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    TodoRepository todoRepository;

    @PostMapping(consumes = "application/json")
    public Mono<Todo> insertTodo(@RequestBody Todo todo) {
        return todoRepository.save(todo);
    }

    @GetMapping
    public Flux<Todo> getTodoList(){
        return todoRepository.findAll();
    }

    @GetMapping("/{task_no}")
    public Mono<Todo> getTodoItem(@PathVariable Integer task_no){
        return todoRepository.findById(task_no);
    }

    @PutMapping("/{task_no}")
    public Mono<Todo> updateTask(@RequestBody Todo todo, @PathVariable Integer task_no){
        return todoRepository.findById(task_no)
                .map(t -> {
                    t.setSubject(todo.getSubject());
                    return t;
                }).flatMap(t-> todoRepository.save(t));
    }

}
