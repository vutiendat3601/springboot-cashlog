package com.dv.cashlog.io.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Where;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Where(clause = "is_deleted = 0")
@Entity(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    private String description;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<UserEntity> users;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String createdBy;

    private String updatedBy;

    private Boolean isDeleted;
}
