package com.example.reactivepostgredemo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@Setter
public class Customer {

    @Id
    private Integer id;
    @Column
    private String name;

}
