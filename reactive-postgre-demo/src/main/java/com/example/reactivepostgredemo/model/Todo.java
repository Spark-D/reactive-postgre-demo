package com.example.reactivepostgredemo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Data
public class Todo {
    @Id
    private String task_no;
    @Column
    private String subject;
    @Column
    private Integer id;
    @Column
    private String finish_yn;
    @Column
    private LocalDateTime sys_reg_dt;
    @Column
    private LocalDateTime sys_mod_dt;
    @Column
    private Integer sys_reg_id;
    @Column
    private Integer sys_mod_id;
}
