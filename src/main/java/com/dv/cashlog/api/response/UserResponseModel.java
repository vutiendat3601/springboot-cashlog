package com.dv.cashlog.api.response;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseModel {
    private Long id;
    private String email;
    private String phone;
    private String fullName;
    private String userCode;
    private LocalDate birthday;
    private Long roleId;
}
