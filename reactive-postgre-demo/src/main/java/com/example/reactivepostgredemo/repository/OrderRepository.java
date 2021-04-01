package com.example.reactivepostgredemo.repository;

import com.example.reactivepostgredemo.model.OmOd;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface OrderRepository extends ReactiveCrudRepository<OmOd, String> {

    @Query("SELECT 'T'|| to_char(now(), 'YYYYMMDD') || nextval('todo_task_no_seq')")
    Mono<String> getSeq();

    Mono<OmOd> findByOdNo(String s);
}
