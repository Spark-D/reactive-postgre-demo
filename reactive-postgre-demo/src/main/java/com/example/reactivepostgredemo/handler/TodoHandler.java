package com.example.reactivepostgredemo.handler;

import com.example.reactivepostgredemo.model.Todo;
import com.example.reactivepostgredemo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class TodoHandler {

    @Autowired
    TodoRepository todoRepository;

    @CrossOrigin
    public Mono<ServerResponse> findAll(ServerRequest serverRequest){
        Flux<Todo> todoFlux = todoRepository.findAll();

        return ServerResponse.ok()
                .body(todoFlux, Todo.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest){
        Integer task_no = Integer.valueOf(serverRequest.pathVariable("task_no"));

        return todoRepository.findById(task_no)
                .flatMap(todo -> ServerResponse.ok().bodyValue(todo))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest){
        System.out.println("repo save!!!!!!!!!!!!!!");
        Mono<Todo> todoMono = serverRequest.bodyToMono(Todo.class)
                .flatMap(todoRepository::save);

        return ServerResponse.status(HttpStatus.CREATED).body(todoMono, Todo.class);
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest){
        Integer task_no = Integer.valueOf(serverRequest.pathVariable("task_no"));

        return todoRepository.findById(task_no)
                .flatMap(todo -> {
                    Mono<Todo> update = serverRequest.bodyToMono(Todo.class)
                            .flatMap(t -> todoRepository.modify(t.getSubject(), t.getTask_no()));
                    return ServerResponse.ok().body(update, Todo.class);
                })
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest){
        Integer task_no = Integer.valueOf(serverRequest.pathVariable("task_no"));

        return todoRepository.findById(task_no)
                .flatMap(todo -> {
                    Mono<Void> delete = todoRepository.deleteById(todo.getTask_no());
                    return ServerResponse.ok().body(delete, Void.class);
                })
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
