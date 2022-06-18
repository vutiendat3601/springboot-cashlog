package com.dv.cashlog.api.response;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private long id;
    private String email;
    private String phone;
    private String fullName;
    private LocalDate birthday;
    private String userCode;
    private RoleResponse role;
}
