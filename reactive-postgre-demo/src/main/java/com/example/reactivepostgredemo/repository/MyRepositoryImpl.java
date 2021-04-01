package com.example.reactivepostgredemo.repository;

import com.example.reactivepostgredemo.model.Customer;
import com.example.reactivepostgredemo.model.OmOd;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;

@Component
@RequiredArgsConstructor
public class MyRepositoryImpl implements MyRepository {

    private final R2dbcEntityTemplate template;

    @Override
    public Mono<Customer> findByCompositeKey(Integer custId, String name) {
        return template.select(Customer.class)
                .matching(Query.query(where("id").is(custId).and("name").is(name)))
                .one();
    }

    @Override
    public Mono<OmOd> findByOdNo(String odNo) {
        return template.select(OmOd.class)
                .matching(Query.query(where("od_no").is(odNo))).one();
    }


}
