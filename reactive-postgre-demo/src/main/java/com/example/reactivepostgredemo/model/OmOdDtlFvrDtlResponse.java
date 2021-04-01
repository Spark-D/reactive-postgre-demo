package com.example.reactivepostgredemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OmOdDtlFvrDtlResponse implements Serializable  {
    private static final long serialVersionUID = 9206427824695736116L;

    private String odNo;
    private String mbNo;
    private Integer odSeq;
    private Integer procSeq;
    private Integer odQty;
    private Integer cnclQty;
    private Integer slPrc;
    private Integer dcAmt;
    private String dcTnnoCd;
    private Integer fvrAmt;
    private String prNo;
    private String prNm;
    private String cpnNo;
    private String cpnNm;
}
