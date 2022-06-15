package com.dv.cashlog.common.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id; //
    private String email;
    private String phone;
    private String password;
    private String encryptedPasssword; //
    private Boolean verifedEmail; //
    private String fullName;
    private LocalDate birhtday;
    private String userCode;
    private Long roleId;
    private RoleDto role; //
}
