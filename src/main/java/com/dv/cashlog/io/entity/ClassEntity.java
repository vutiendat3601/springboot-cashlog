package com.dv.cashlog.io.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
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
@Entity(name = "classes")
public class ClassEntity implements Serializable {

    private final static long serialVersionUID = 1l;

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "clazz", cascade = CascadeType.MERGE)
    private List<UserEntity> users;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "major_id")
    private MajorEntity major;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String createdBy;

    private String updatedBy;

    private Boolean isDeleted;
}
