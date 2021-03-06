package com.example.reactivepostgredemo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("Om_Od_Dtl")
public class OmOdDtl implements Persistable<String>, Serializable {
    private static final long serialVersionUID = -5200824753588401614L;

    @Id
    private String odNo;
    private Integer odSeq;
    private Integer procSeq;
    private String clmNo;
    private String odTypCd;
    private String odPrgsStepCd;
    private String mbNo;
    private Integer odQty;
    private Integer cnclQty;
    private Integer rtngQty;
    private Integer xchgQty;
    private Integer slPrc;
    private Integer dcAmt;
    private String pdNo;
    private String pdNm;
    private String prNo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime odCmptDttm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime purCfrmDttm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime regDttm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modDttm;

    @Transient
    private boolean newOrder;

    @Override
    public String getId() {
        return odNo;
    }

    @Override
    public boolean isNew() {
        return newOrder;
    }
}
