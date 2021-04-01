package com.example.reactivepostgredemo.repository;

import com.example.reactivepostgredemo.model.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {


}
