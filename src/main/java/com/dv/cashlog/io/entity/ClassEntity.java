package com.dv.cashlog.io.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "classes")
public class ClassEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;
}
