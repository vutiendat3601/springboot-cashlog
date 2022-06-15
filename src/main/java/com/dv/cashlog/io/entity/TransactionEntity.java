package com.dv.cashlog.io.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private LocalDateTime dateTime;

    @Column
    private String particular;

    @Column
    private Double collected;

    @Column
    private Double paid;

    @Column
    private String src_money;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
