package com.dv.cashlog.common.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MajorDto {

    private Long id;

    private String name;

    private String description;
    
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String createdBy;

    private String updatedBy;

    private Boolean isDeleted;
}
