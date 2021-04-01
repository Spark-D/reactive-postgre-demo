package com.example.reactivepostgredemo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Getter
@Setter
public class Comment {

    @Id
    private Integer comment_no;
    private String task_no;
    private String contents;
    private Integer comm_depth;
    private LocalDateTime sys_reg_dt;
    private LocalDateTime sys_mod_dt;
    private Integer sys_reg_id;
    private Integer sys_mod_id;

}
