package com.example.reactivepostgredemo.repository;

import com.example.reactivepostgredemo.model.OmOdFvrDtl;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface OrderFavorRepository extends ReactiveCrudRepository<OmOdFvrDtl, String> {

    @Query("select * from om_od_fvr_dtl where od_no = :od_no")
    Flux<OmOdFvrDtl> findByOdNo(String odNo);
}
