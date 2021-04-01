package com.example.reactivepostgredemo.handler;

import com.example.reactivepostgredemo.model.Comment;
import com.example.reactivepostgredemo.model.Todo;
import com.example.reactivepostgredemo.model.TodoDto;
import com.example.reactivepostgredemo.model.TodoProjection;
import com.example.reactivepostgredemo.repository.CommentRepository;
import com.example.reactivepostgredemo.repository.TodoRepository;
import com.example.reactivepostgredemo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
public class TodoHandler {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    TodoService todoService;

    @CrossOrigin
    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        Flux<Todo> todoFlux = todoRepository.findAll();

        return ServerResponse.ok()
                .body(todoFlux, Todo.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        String task_no = serverRequest.pathVariable("task_no");

        return todoRepository.findById(task_no)
                .flatMap(todo -> ServerResponse.ok().bodyValue(todo))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        System.out.println("repo save!!!!!!!!!!!!!!");
        Mono<Todo> todoMono = serverRequest.bodyToMono(Todo.class)
                .flatMap(todoRepository::save);

        return ServerResponse.status(HttpStatus.CREATED).body(todoMono, Todo.class);
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        String task_no = serverRequest.pathVariable("task_no");

        return todoRepository.findById(task_no)
                .flatMap(todo -> {
                    Mono<Todo> update = serverRequest.bodyToMono(Todo.class)
                            .flatMap(t -> todoRepository.modify(t.getSubject(), t.getTask_no()));
                    return ServerResponse.ok().body(update, Todo.class);
                })
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        String task_no = serverRequest.pathVariable("task_no");

        return todoRepository.findById(task_no)
                .flatMap(todo -> {
                    Mono<Void> delete = todoRepository.deleteById(todo.getTask_no());
                    return ServerResponse.ok().body(delete, Void.class);
                })
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<Todo> findTodoComments(ServerRequest serverRequest) {
        String task_no = serverRequest.pathVariable("task_no");
        Mono<Todo> todoMono = todoRepository.findById(task_no).cache();
        Flux<Comment> commentFlux = commentRepository.findCommentsByTodoNo(task_no).cache();

        return todoMono.zipWith(commentFlux.collectList())
                .map(combine -> combine.getT1().withCommentList(combine.getT2()));
    }

    public Mono<ServerResponse> getTodoWithComments(ServerRequest serverRequest) {
        Mono<Todo> todoMono = this.findTodoComments(serverRequest).log();

        return ServerResponse.ok()
                .body(todoMono, Todo.class);
    }

    public Flux<Todo> findTodoListComments(ServerRequest serverRequest) {
//        Flux<Todo> todoListAll = todoRepository.findAll().log();
        Flux<Todo> todoAllListWithComments = todoRepository.findAll().cache().flatMap(t ->
                Mono.just(t)
                        .zipWith(commentRepository.findCommentsByTodoNo(t.getTask_no()).cache().collectList())
                        .map(combine -> combine.getT1().withCommentList(combine.getT2())).log()
        );

        return todoAllListWithComments;
    }

    public Mono<ServerResponse> getAllWithComments(ServerRequest serverRequest) {
        Flux<Todo> todoWithCommentsList = this.findTodoListComments(serverRequest);

        return ServerResponse.ok()
                .body(todoWithCommentsList, Todo.class);
    }

    public Mono<ServerResponse> getAllByInterface(ServerRequest serverRequest) {
        Flux<TodoProjection> todoProjectionFlux = todoService.getAllByProjection();
        return  ServerResponse.ok()
                .body(todoProjectionFlux, TodoProjection.class);
    }

    public Mono<ServerResponse> getAllByDto(ServerRequest serverRequest){
        Flux<TodoDto> todoProjectionFlux = todoService.getAllByDto();

        return ServerResponse.ok()
                .body(todoProjectionFlux, TodoDto.class);
    }


}
