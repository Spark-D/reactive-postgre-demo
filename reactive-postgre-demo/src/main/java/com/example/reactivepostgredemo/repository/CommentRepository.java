package com.example.reactivepostgredemo.repository;

import com.example.reactivepostgredemo.model.Comment;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CommentRepository extends ReactiveCrudRepository<Comment, Integer> {

    @Query("select * from comment where task_no = :task_no")
    Flux<Comment> findCommentsByTodoNo(String task_no);


}
