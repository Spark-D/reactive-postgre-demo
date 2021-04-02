package com.example.reactivepostgredemo.config;

import com.example.reactivepostgredemo.model.OmOd;
import com.example.reactivepostgredemo.repository.OrderRepository;
import com.example.reactivepostgredemo.service.OrderService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Configuration
@EnableWebFlux
public class OrderCallbackConfig implements BeforeSaveCallback<OmOd>, BeforeConvertCallback<OmOd> {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    public Publisher<OmOd> onBeforeConvert(OmOd OmOd, SqlIdentifier table) {
//        System.out.println("onBeforeConvert!!!!!!!!!!!!!!!!"+ table);
        if (OmOd.getOdNo() == null || OmOd.getOdNo() == "") {
            return orderService.makeOdNo().map(odNo -> {
                OmOd.setModDttm(LocalDateTime.now());
                OmOd.setRegDttm(LocalDateTime.now());
                OmOd.setOdNo(odNo);
                return OmOd;
            }).log("od_no regist ::::: "+ OmOd.toString());
        } else {
            OmOd.setModDttm(LocalDateTime.now());
            Mono<OmOd> modiOmOd = Mono.just(OmOd);
            return modiOmOd;
        }
    }

    @Override
    public Publisher<OmOd> onBeforeSave(OmOd entity, OutboundRow row, SqlIdentifier table) {
        Mono<OmOd> OmOd = Mono.just(entity);
        return OmOd;
    }



}
