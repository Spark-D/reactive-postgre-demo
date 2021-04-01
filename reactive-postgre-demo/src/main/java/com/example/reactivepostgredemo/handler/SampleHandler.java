package com.example.reactivepostgredemo.handler;

import com.example.reactivepostgredemo.model.Customer;
import com.example.reactivepostgredemo.repository.CustomerRepository;
import com.example.reactivepostgredemo.repository.MyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SampleHandler {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    MyRepository myRepository;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest){

        Flux<Customer> customerFlux = customerRepository.findAll();

        return ServerResponse.ok()
                .body(customerFlux, Customer.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest){
        Integer custId = Integer.valueOf(serverRequest.pathVariable("id"));

        return customerRepository.findById(custId)
                .flatMap(customer -> ServerResponse.ok().bodyValue(customer))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest){
        Mono<Customer> customerMono = serverRequest.bodyToMono(Customer.class)
                .flatMap(customerRepository::save);

        return ServerResponse.status(HttpStatus.CREATED).body(customerMono, Customer.class);
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest){
        Integer custId = Integer.valueOf(serverRequest.pathVariable("id"));

        return customerRepository.findById(custId)
                .flatMap(customer -> {
                    Mono<Customer> update = serverRequest.bodyToMono(Customer.class)
                            .flatMap(customerRepository::save);

                    return ServerResponse.ok().body(update, Customer.class);
                })
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest){
        Integer custId = Integer.valueOf(serverRequest.pathVariable("id"));

        return customerRepository.findById(custId)
                .flatMap(customer -> {
                    Mono<Void> delete = customerRepository.deleteById(customer.getId());
                    return ServerResponse.ok().body(delete, Void.class);
                })
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * composite key로 조회해오기
     * */
    public Mono<ServerResponse> getCompositeKeyCustomer( ServerRequest serverRequest){
        Integer custId = Integer.valueOf(serverRequest.pathVariable("id"));
        String name = serverRequest.pathVariable("name");

        Mono<Customer> customerFlux = myRepository.findByCompositeKey(custId, name).log();

        return ServerResponse.status(HttpStatus.CREATED).body(customerFlux, Customer.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
