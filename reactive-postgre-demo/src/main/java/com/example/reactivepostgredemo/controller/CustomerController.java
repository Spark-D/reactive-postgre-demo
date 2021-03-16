package com.example.reactivepostgredemo.controller;

import com.example.reactivepostgredemo.model.Customer;
import com.example.reactivepostgredemo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @GetMapping
    public Flux<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Customer> getCustomer(@PathVariable Integer id) {
        return customerRepository.findById(id);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(consumes = {"application/x-www-form-urlencoded"})
    public Mono<Customer> insertCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @PutMapping("/{id}")
    public Mono<Customer> updateCustomer(@RequestBody Customer customer, @PathVariable Integer id){
        return customerRepository.findById(id)
                .map(c -> {
                    c.setName(customer.getName());
                    return c;
                }).flatMap(c-> customerRepository.save(c));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomer(@PathVariable Integer id){
        return customerRepository.deleteById(id);
    }

}
