package com.example.reactivepostgredemo.repository;

import com.example.reactivepostgredemo.model.Customer;
import com.example.reactivepostgredemo.model.OmOd;
import reactor.core.publisher.Mono;

//projection
public interface MyRepository {

    Mono<Customer> findByCompositeKey(Integer custId, String name);

    Mono<OmOd> findByOdNo(String odNo);
}
