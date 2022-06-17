package com.dv.cashlog.api.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String email;
    private String password;
    private String phone;
    private String fullName;
    private LocalDate birthday;
    private String userCode;
    private String roleName;
}
