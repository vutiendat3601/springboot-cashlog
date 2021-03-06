package com.dv.cashlog.io.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Where;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Where(clause = "is_deleted = 0")
@Entity(name = "transactions")
public class TransactionEntity implements Serializable {

    private final static long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime dateTime;

    @Column
    private String particular;

    @Column
    private Double collected;

    @Column
    private Double paid;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "bank_id")
    private BankEntity bank;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String createdBy;

    private LocalDateTime createdDate;

    private String updatedBy;

    private LocalDateTime updatedDate;

    private Boolean isDeleted;
}
