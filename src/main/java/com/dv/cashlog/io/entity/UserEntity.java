package com.dv.cashlog.io.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Where;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Where(clause = "is_deleted = 0")
@Entity(name = "users")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String phone;

    private String encryptedPassword;

    private Boolean verifiedEmail;

    private String fullName;

    private LocalDate birthday;

    private String userCode;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "class_id")
    private ClassEntity clazz;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
    private List<TransactionEntity> transactions;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    private String createdBy;

    private LocalDateTime createdDate;

    private String updatedBy;

    private LocalDateTime updatedDate;

    private Boolean isDeleted;
}
