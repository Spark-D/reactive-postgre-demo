package com.example.reactivepostgredemo.config;

import com.example.reactivepostgredemo.model.Todo;
import com.example.reactivepostgredemo.repository.TodoRepository;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.SettableValue;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.r2dbc.core.Parameter;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Configuration
@EnableWebFlux
public class CallbackConfig implements BeforeSaveCallback<Todo>, BeforeConvertCallback<Todo> {

    @Autowired
    TodoRepository todoRepository;

    public Publisher<Todo> onBeforeConvert(Todo todo, SqlIdentifier table) {
        System.out.println("onBeforeConvert!!!!!!!!!!!!!!!!"+ table);
        if (todo.getTask_no() == null || todo.getTask_no()  == "") {
            return todoRepository.getSeq().map(seq -> {
                todo.setSys_reg_dt(LocalDateTime.now());
                todo.setTask_no(seq);
                return todo;
            }).log("new sequence :::" + todo.toString());
        } else {
            todo.setSys_mod_dt(LocalDateTime.now());
            Mono<Todo> modiTodo = Mono.just(todo);
            return modiTodo;
        }
    }

    public Publisher<Todo> onBeforeSave(Todo todo, OutboundRow row, SqlIdentifier table) {
        System.out.println("onBeforeSave!!!!!!!!!!!!!!!!" + todo.toString());
        System.out.println("onBeforeSave!!!!!!!!!!!!!!!!" + row);
        System.out.println("onBeforeSave!!!!!!!!!!!!!!!!" + table);
        return null;
    }

}
