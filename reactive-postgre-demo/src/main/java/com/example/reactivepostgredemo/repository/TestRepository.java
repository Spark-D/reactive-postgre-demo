package com.example.reactivepostgredemo.repository;

import com.example.reactivepostgredemo.model.Person;
import com.example.reactivepostgredemo.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class TestRepository {
    @Autowired
    DatabaseClient client;

    public Flux<String> getStringPerson() {
//        Flux<Map<String, Object>> test = client.sql("select * from person").fetch().all();
//
//        test.subscribe(x -> {
//            Logger.log("x.get(lastname) : {}", x.get("lastname"));
//        });

        Flux<String> lastnames = client.sql("select * from person")
                .map(row -> row.get("lastname", String.class))
                .all();

        return lastnames;
    }

    public Flux<Person> getPersonList() {

        Logger.log("getPersonList ::::::::::::");
        client.sql("select * from person")
                .fetch()
                .all()
                .log()
                .flatMap(data -> System.out::println);

        return Flux.just(new Person());
    }
}
