package com.dv.cashlog.common.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassDto {
    private Long id;

    private String name;

    private String description;

    private String nameOfMajor;
    
    private MajorDto major;

    private List<UserDto> users;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String createdBy;

    private String updatedBy;

    private Boolean isDeleted;
}
