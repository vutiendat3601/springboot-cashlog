package com.dv.cashlog.io.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "users")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false, length = 30)
    private String encryptedPassword;

    @Column
    private Boolean verifiedEmail;

    @Column
    private String fullName;

    @Column
    private LocalDate birthDay;

    @Column
    private String userCode;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TransactionEntity> transactions;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;
}
