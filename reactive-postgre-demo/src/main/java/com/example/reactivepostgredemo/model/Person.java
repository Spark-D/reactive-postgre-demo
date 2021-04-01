package com.example.reactivepostgredemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Person {
    @Id
    private Long id;
    @Column
    private String firstname;
    @Column
    private String lastname;
}
