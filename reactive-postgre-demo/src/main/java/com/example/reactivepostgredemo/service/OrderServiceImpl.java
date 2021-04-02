package com.example.reactivepostgredemo.service;

import com.example.reactivepostgredemo.model.OmOd;
import com.example.reactivepostgredemo.model.OmOdDtl;
import com.example.reactivepostgredemo.model.OmOdDtlFvrDtlResponse;
import com.example.reactivepostgredemo.model.OmOdFvrDtl;
import com.example.reactivepostgredemo.repository.MyRepository;
import com.example.reactivepostgredemo.repository.OrderDetailRepository;
import com.example.reactivepostgredemo.repository.OrderFavorRepository;
import com.example.reactivepostgredemo.repository.OrderRepository;
import com.example.reactivepostgredemo.util.Logger;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

@Service
public class OrderServiceImpl implements OrderService {


    private static final BiFunction<Row, RowMetadata,OmOdDtlFvrDtlResponse> MAPPING_FUNCTION = (row, rowMetaData) ->OmOdDtlFvrDtlResponse.builder()
            .odNo(row.get("od_no", String.class))
            .mbNo(row.get("mb_no", String.class))
            .odSeq(row.get("od_seq", Integer.class))
            .procSeq(row.get("proc_seq", Integer.class))
            .prNo(row.get("pr_no", String.class))
            .build();

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    OrderFavorRepository orderFavorRepository;

    @Autowired
    MyRepository myRepository;

    @Autowired
    DatabaseClient databaseClient;

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

    @Override
    public Flux<OmOdDtlFvrDtlResponse> getClientData() {

//        Flux<Map<String, Object>> resultFlux =
//                databaseClient.sql("SELECT * FROM OM_OD WHERE OD_NO = :odNo")
//                .bind("odNo", "T20210402151")
//                .fetch()
//                .all();

        return databaseClient.sql(
                "select *\n" +
                        " from om_od_dtl as a\n" +
                        "    , om_od_fvr_dtl as b\n" +
                        "where a.od_no = :odNo\n" +
                        "  and a.od_no = b.od_no\n" +
                        "  and a.od_seq = b.od_seq\n" +
                        "  and a.proc_seq = b.proc_seq\n")
                .bind("odNo", "T20210402152")
                .fetch()
                .all()
                .map(row -> getReturnValue(new OmOdDtlFvrDtlResponse(), row));
    }

    private  <T> T getReturnValue(T t, Map<String, Object> row) {
        Field[] fields = t.getClass().getDeclaredFields();
        for(Field field : fields) {
            field.setAccessible(true);
            try {
                if( !field.isAnnotationPresent(Transient.class) ) {
                    field.set(t, row.get(camelToSnake(field.getName())));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    private String camelToSnake(String camelStr) {
        if(camelStr == null){return "";}
        // Regular Expression
        String regex = "([a-z])([A-Z]+)";

        // Replacement string
        String replacement = "$1_$2";

        // Replace the given regex
        // with replacement string
        // and convert it to lower case.
        // return string
        return camelStr.replaceAll(regex, replacement).toLowerCase();
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


    @Override
    public Flux<OmOdDtlFvrDtlResponse> getClientBifunList() {
        return databaseClient.sql("select ood.od_no, ood.mb_no, ood.od_seq ,ood.proc_seq , ood.od_qty , ood.cncl_qty, \n" +
                "ood.sl_prc , ood.dc_amt , oofd.dc_tnno_cd , oofd.fvr_amt , oofd.pr_no, \n" +
                "oofd.pr_nm ,oofd.cpn_nm, oofd.cpn_nm \n" +
                "from om_od_dtl ood , om_od_fvr_dtl oofd\n" +
                "where ood.od_no = oofd.od_no \n" +
                "  and ood.od_seq = oofd.od_seq \n" +
                "  and ood.proc_seq = oofd.proc_seq \n" +
                "  and ood.od_no = :od_no")
                .bind("od_no", "T20210402152")
                .map(MAPPING_FUNCTION)
                .all();
    }

    /**
     * 주문취소
     * */
    @Override
    public Mono<OmOd> cancelOrder(OmOd omOd) {

        //해당주문있는지 확인.
        //om_od 그 주문 mod dtime 수정
        //om_od_dtl 취소한 품목 원건 그대로 생성, 원처리순번 넣고, 클레임번호/처리순번 +1
        //om_od_fvr_dtl 취소한 품목 원건 그대로 생성, 클레임번호/원주문혜택번호 넣고,혜택구분코드 CNCL, 처리순번 +1


       return null;
    }


}
