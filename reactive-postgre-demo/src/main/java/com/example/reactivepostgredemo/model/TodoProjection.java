package com.example.reactivepostgredemo.model;

import org.springframework.beans.factory.annotation.Value;

/**
 * Open Projection
 * 접근자 메소드로 새로운 값을 계산하기 위해 사용.
 */
public interface TodoProjection {

    String getTask_no();
    String getSubject();

    @Value("#{target.task_no + ' ' + target.subject}")
    String getJoinTd();



}
