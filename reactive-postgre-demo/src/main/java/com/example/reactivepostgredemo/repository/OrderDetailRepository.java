package com.example.reactivepostgredemo.repository;

import com.example.reactivepostgredemo.model.OmOdDtl;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface OrderDetailRepository extends ReactiveCrudRepository<OmOdDtl, String> {

    @Query("select * from om_od_dtl where od_no = :od_no")
    Flux<OmOdDtl> findByOdNo(String s);
}
