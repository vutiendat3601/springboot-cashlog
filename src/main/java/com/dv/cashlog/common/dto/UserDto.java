package com.dv.cashlog.common.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String email;
    private String password;
    private String phone;
    private String fullName;
    private LocalDate birthday;
    private String userCode;
    private String nameOfRole;
    private RoleDto role;
    private String createdBy;
    private LocalDateTime createdDate;
    private String updatedBy;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;
}
