package com.example.reactivepostgredemo.repository;

import com.example.reactivepostgredemo.model.Todo;
import com.example.reactivepostgredemo.model.TodoDto;
import com.example.reactivepostgredemo.model.TodoProjection;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TodoRepository extends ReactiveCrudRepository<Todo, String>{

    @Query("UPDATE todo SET subject=:subject, sys_mod_dt=NOW() WHERE task_no=:task_no")
    Mono<Todo> modify(String subject, String task_no);

    @Query("SELECT 'T'|| to_char(now(), 'YYYYMMDDHH24MISSMS') || nextval('todo_task_no_seq')")
    Mono<String> getSeq();

    @Query("DELETE from todo where task_no = :task_no")
    Mono<Void> deleteById(String task_no);

    @Query("select * from todo where task_no = :task_no")
    Mono<Todo> findById(String task_no);
//
    Flux<TodoProjection> findAllBy();

    Flux<TodoDto> getAllBy();
}
