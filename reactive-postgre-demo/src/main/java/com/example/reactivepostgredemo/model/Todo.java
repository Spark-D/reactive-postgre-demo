package com.example.reactivepostgredemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    private String task_no;
    private String subject;
    private Integer id;
    private String finish_yn;
    private LocalDateTime sys_reg_dt;
    private LocalDateTime sys_mod_dt;
    private Integer sys_reg_id;
    private Integer sys_mod_id;

    @With
    @Transient
    private List<Comment> commentList;

}
