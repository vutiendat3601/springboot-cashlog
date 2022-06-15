package com.dv.cashlog.api.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestModel {
    private String email;
    private String phone;
    private String password;
    private String fullName;
    private String userCode;
    private LocalDate birthday;
    private Long roleId;
}
