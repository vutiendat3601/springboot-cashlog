package com.dv.cashlog.common.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String email;
    private String phone;
    private String password;
    private String fullName;
    private LocalDate birthday;
    private String userCode;
    private Long id; //
    private String encryptedPassword; //
    private Boolean verifedEmail; //
    private RoleDto role; //
}
