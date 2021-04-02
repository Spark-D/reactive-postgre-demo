package com.example.reactivepostgredemo.service;

import com.example.reactivepostgredemo.model.OmOd;
import com.example.reactivepostgredemo.model.OmOdDtl;
import com.example.reactivepostgredemo.model.OmOdFvrDtl;
import com.example.reactivepostgredemo.repository.MyRepository;
import com.example.reactivepostgredemo.repository.OrderDetailRepository;
import com.example.reactivepostgredemo.repository.OrderFavorRepository;
import com.example.reactivepostgredemo.repository.OrderRepository;
import com.example.reactivepostgredemo.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    OrderFavorRepository orderFavorRepository;

    @Autowired
    MyRepository myRepository;

    @Override
    public Flux<OmOd> getOrderList() {
        return getAllOrderInfoList();
    }

    private Flux<OmOd> getAllOrderInfoList() {
        // 모든주문디테일
        Flux<OmOd> orderDetailMappingFlux = getOrderDtlMappingFlux();
        // 모든주문혜택 맵핑..
        Flux<OmOd> orderDtlAndFvrMappingFlux = orderDetailMappingFlux
                .flatMap(omOd -> Mono.just(omOd).zipWith(
                        orderFavorRepository.findByOdNo(omOd.getOdNo()).collectList())
                        .map(data -> data.getT1().withOmOdFvrDtlList(data.getT2())));
        return orderDtlAndFvrMappingFlux;
    }


    private Flux<OmOd> getOrderDtlMappingFlux() {
        Flux<OmOd> omOdFlux = orderRepository.findAll();
        Flux<List<OmOdDtl>> omOdDtlListFlux = omOdFlux.map(OmOd::getOdNo)
                .flatMap(odNo-> orderDetailRepository.findByOdNo(odNo).collectList());

        return Flux.zip(omOdFlux, omOdDtlListFlux, (t1, t2)-> t1.withOmOdDtlList(t2));
    }

    @Override
    public Mono<OmOd> findByOdNo(String odNo) {
        return myRepository.findByOdNo(odNo);
    }

       @Override
    public Mono<String> makeOdNo() {
        Mono<String> regOdNo = orderRepository.getSeq();
        return regOdNo;
    }

    @Override
    public Mono<OmOd> getWithDataOrderItem(String odNo) {
        Mono<OmOd> omOdMono = orderRepository.findByOdNo(odNo)
                .flatMap(omOd -> getOmOdDtlListByOdNo(omOd))
                .flatMap(omOd -> getOmOdFvrDtlListByOdNo(omOd));
        return omOdMono;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED , rollbackFor = Exception.class)
    public Mono<OmOd> createOrder(OmOd omOd) {
        omOd.setNewOrder(true);
        omOd.setRegDttm(LocalDateTime.now());
        Mono<OmOd> omOdMono = orderRepository.save(omOd)
                .log("create!!"+ omOd.toString())
                .flatMap(this::createOrderDetail)
                .flatMap(this::createOrderFavor);
        return omOdMono;
    }

    private Mono<OmOd> createOrderDetail(OmOd od) {

        Logger.log("createOrderDetail  ::::::::::"+ od.getOdNo());
        AtomicInteger atomicInteger = new AtomicInteger(1);
        Flux<OmOdDtl> omOdDtl = Flux.fromIterable(od.getOmOdDtlList()).flatMap(dtl -> {
                  dtl.setOdNo(od.getOdNo());
                  dtl.setRegDttm(LocalDateTime.now());
                  dtl.setOdTypCd("10");
                  dtl.setProcSeq(1);
                  dtl.setOdSeq(atomicInteger.getAndIncrement());
                  dtl.setNewOrder(true);
                  return orderDetailRepository.save(dtl);
                });

        return Mono.just(od).zipWith(omOdDtl.collectList())
                .map(data -> data.getT1().withOmOdDtlList(data.getT2()));
    }

    private Mono<OmOd> createOrderFavor(OmOd od) {
        Logger.log("createOrderFavor  ::::::::::"+ od.getOdNo());
        AtomicInteger atomicInteger = new AtomicInteger(1);
        Flux<OmOdFvrDtl> omOdFvrDtlFlux =  Flux.fromIterable(od.getOmOdFvrDtlList()).flatMap(fvr -> {
            fvr.setOdNo(od.getOdNo());
            fvr.setProcSeq(1);
            fvr.setOdSeq(atomicInteger.getAndIncrement());
            fvr.setOdFvrDvsCd("HAPN");
            fvr.setRegDttm(LocalDateTime.now());
            fvr.setNewOrder(true);
            return orderFavorRepository.save(fvr);
        });

        return Mono.just(od).zipWith(omOdFvrDtlFlux.collectList())
                .map(data -> data.getT1().withOmOdFvrDtlList(data.getT2()));
    }

    /**
     * om_od_dtl 조립
     * */
    private Mono<OmOd> getOmOdDtlListByOdNo(OmOd omOd){
        return Mono.just(omOd).zipWith(
                orderDetailRepository.findByOdNo(omOd.getOdNo()).collectList())
                .map(data -> data.getT1().withOmOdDtlList(data.getT2()));
    }

    /**
     * om_od_fvr_dtl 조립
     * */
    private Mono<OmOd> getOmOdFvrDtlListByOdNo(OmOd omOd) {
        return Mono.just(omOd).zipWith(
                orderFavorRepository.findByOdNo(omOd.getOdNo()).collectList())
                .map(data -> data.getT1().withOmOdFvrDtlList(data.getT2()));
    }






}
