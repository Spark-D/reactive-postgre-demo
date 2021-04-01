package com.example.reactivepostgredemo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("om_od")
public class OmOd implements Persistable<String>, Serializable {
    private static final long serialVersionUID = -5793348114310316331L;

    @Id
    private String odNo;
    private String mbNo;
    private String odrNm;
    private String orglOdNo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime odCmptDttm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime regDttm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modDttm;

    @With
    @Transient
    private List<OmOdDtl> omOdDtlList;

    @With
    @Transient
    private List<OmOdFvrDtl> omOdFvrDtlList;

    @Transient
    private boolean newOrder;

    @Override
    public String getId() {
        return odNo;
    }

    @Override
    public boolean isNew() { return newOrder;}
}
